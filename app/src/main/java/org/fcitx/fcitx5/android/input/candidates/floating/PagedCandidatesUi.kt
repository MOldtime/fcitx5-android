/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2024 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.candidates.floating

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.launch
import org.fcitx.fcitx5.android.core.FcitxEvent
import org.fcitx.fcitx5.android.core.FcitxEvent.PagedCandidateEvent.LayoutHint
import org.fcitx.fcitx5.android.daemon.FcitxConnection
import org.fcitx.fcitx5.android.daemon.launchOnReady
import org.fcitx.fcitx5.android.data.InputFeedbacks
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.FcitxInputMethodService
import org.fcitx.fcitx5.android.utils.item
import splitties.resources.styledColor
import splitties.views.dsl.core.Ui
import splitties.views.dsl.recyclerview.recyclerView

class PagedCandidatesUi(
    override val ctx: Context,
    val theme: Theme,
    fcitx: FcitxConnection,
    service: FcitxInputMethodService,
    private val setupTextView: TextView.() -> Unit
) : Ui {

    private var data = FcitxEvent.PagedCandidateEvent.Data.Empty
    private var currentPage = -1
    private var currentTotal = -1
    private var isVertical = false
    private var candidateActionMenu: PopupMenu? = null

    sealed class UiHolder(open val ui: Ui) : RecyclerView.ViewHolder(ui.root) {
        class Candidate(override val ui: LabeledCandidateItemUi) : UiHolder(ui)
        class Pagination(override val ui: PaginationUi) : UiHolder(ui)
    }

    private val candidatesAdapter = object : RecyclerView.Adapter<UiHolder>() {
        override fun getItemCount() =
            data.candidates.size + (if (data.hasPrev || data.hasNext) 1 else 0)

        override fun getItemViewType(position: Int) = if (position < data.candidates.size) 0 else 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiHolder {
            return when (viewType) {
                0 -> UiHolder.Candidate(LabeledCandidateItemUi(ctx, theme, setupTextView))
                else -> UiHolder.Pagination(PaginationUi(ctx, theme)).apply {
                    val wrap = ViewGroup.LayoutParams.WRAP_CONTENT
                    ui.root.layoutParams = FlexboxLayoutManager.LayoutParams(wrap, wrap).apply {
                        flexGrow = 1f
                    }
                }
            }
        }

        override fun onBindViewHolder(holder: UiHolder, position: Int) {
            when (holder) {
                is UiHolder.Candidate -> {
                    val idx =
                        (if (data.currentPage == -1) 0 else currentPage * currentTotal) + position
                    val candidate = data.candidates[position]
                    holder.ui.update(candidate, active = position == data.cursorIndex)
                    holder.itemView.setOnClickListener {
                        fcitx.launchOnReady { it.select(idx) }
                    }
                    holder.itemView.setOnLongClickListener {
                        candidateActionMenu?.dismiss()
                        candidateActionMenu = null
                        service.lifecycleScope.launch {
                            val actions = fcitx.runOnReady { getCandidateActions(idx) }
                            if (actions.isEmpty()) return@launch
                            InputFeedbacks.hapticFeedback(holder.ui.root, longPress = true)
                            candidateActionMenu = PopupMenu(ctx, holder.ui.root).apply {
                                menu.add(buildSpannedString {
                                    bold {
                                        color(ctx.styledColor(android.R.attr.colorAccent)) {
                                            append(candidate.text)
                                        }
                                    }
                                }).apply {
                                    isEnabled = false
                                }
                                actions.forEach { action ->
                                    menu.item(action.text) {
                                        fcitx.runIfReady {
                                            triggerCandidateAction(
                                                idx,
                                                action.id
                                            )
                                        }
                                    }
                                }
                                setOnDismissListener {
                                    candidateActionMenu = null
                                }
                                show()
                            }
                        }
                        true
                    }
                }
                is UiHolder.Pagination -> {
                    holder.ui.update(data, service)
                    holder.ui.root.updateLayoutParams<FlexboxLayoutManager.LayoutParams> {
                        width = if (isVertical) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
                        alignSelf = if (isVertical) AlignItems.STRETCH else AlignItems.CENTER
                    }
                }
            }
        }
    }

    private val candidatesLayoutManager = FlexboxLayoutManager(ctx).apply {
        flexWrap = FlexWrap.WRAP
    }

    override val root = recyclerView {
        isFocusable = false
        adapter = candidatesAdapter
        layoutManager = candidatesLayoutManager
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(
        data: FcitxEvent.PagedCandidateEvent.Data,
        orientation: FloatingCandidatesOrientation
    ) {
        this.data = data
        if (data.currentPage == 0) {
            this.currentTotal = data.candidates.size
        }
        this.currentPage = data.currentPage
        this.isVertical = when (orientation) {
            FloatingCandidatesOrientation.Automatic -> data.layoutHint == LayoutHint.Vertical
            else -> orientation == FloatingCandidatesOrientation.Vertical
        }
        candidatesLayoutManager.apply {
            if (isVertical) {
                flexDirection = FlexDirection.COLUMN
                alignItems = AlignItems.STRETCH
            } else {
                flexDirection = FlexDirection.ROW
                alignItems = AlignItems.BASELINE
            }
        }
        candidatesAdapter.notifyDataSetChanged()
    }
}
