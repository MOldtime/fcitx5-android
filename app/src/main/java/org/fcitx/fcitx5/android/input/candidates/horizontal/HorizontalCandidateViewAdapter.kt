/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2024 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.candidates.horizontal

import android.annotation.SuppressLint
import android.view.Gravity.CENTER_VERTICAL
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import org.fcitx.fcitx5.android.core.FcitxEvent
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.candidates.CandidateItemUi
import org.fcitx.fcitx5.android.input.candidates.CandidateViewHolder
import splitties.dimensions.dp
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.wrapContent
import splitties.views.setPaddingDp

open class HorizontalCandidateViewAdapter(val theme: Theme) :
    RecyclerView.Adapter<CandidateViewHolder>() {

    private val indexList = mutableMapOf<Int, Int>() // 存储每页的下标

    private var currentTotal = -1;

    var currentPage: Int = -1 // 页码
        private set

    var candidates: Array<String> = arrayOf()
        private set

    var total = -1
        private set

    var offset = 0
        private set

    @SuppressLint("NotifyDataSetChanged")
    fun updateCandidates(data: FcitxEvent.CandidateListEvent.Data) {
        this.candidates = data.candidates
        this.total = data.total
        if (data.currentPage == 0) {
            this.currentTotal = data.total
        }
        this.currentPage = data.currentPage
        this.offset = if (data.currentPage == -1) 0 else this.currentPage * this.currentTotal
        this.indexList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = candidates.size

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val ui = CandidateItemUi(parent.context, theme)
        ui.root.apply {
            ui.comment.textSize = 10f
            ui.text.textSize = 20f
            addView(ui.comment)
            addView(ui.text)
            minimumWidth = dp(10)
            setPaddingDp(0, 1, 0, 1)
            gravity = CENTER_VERTICAL
            layoutParams = FlexboxLayoutManager.LayoutParams(wrapContent, matchParent)
        }
        return CandidateViewHolder(ui)
    }

    @CallSuper
    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val text = candidates[position]
        val list = text.split(Regex("\\s+"), 2)
        holder.ui.text.text = list[0]
        if (list.size > 1) {
            holder.ui.comment.text = list[1]
            holder.ui.comment.visibility = VISIBLE
        } else {
            holder.ui.comment.visibility = GONE
        }
        holder.text = list[0]
        holder.idx = offset + position
    }
}
