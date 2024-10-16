/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2023 Fcitx5 for Android Contributors
 */
package org.fcitx.fcitx5.android.input.candidates.adapter

import android.annotation.SuppressLint
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.CallSuper
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import org.fcitx.fcitx5.android.core.FcitxEvent
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.candidates.CandidateItemUi
import org.fcitx.fcitx5.android.input.candidates.CandidateViewHolder
import org.fcitx.fcitx5.android.input.keyboard.Page
import splitties.dimensions.dp
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.wrapContent
import splitties.views.setPaddingDp
import timber.log.Timber

open class HorizontalCandidateViewAdapter(val theme: Theme) :
    RecyclerView.Adapter<CandidateViewHolder>() {

    private val indexList = mutableMapOf<Int, Int>() // 存储每页的下标

    private var currentPage: Int = 0 // 页码

    var candidates: Array<String> = arrayOf()
        private set

    var total = -1
        private set

    var offset = 0
        private set

    fun prev(): Int? {
        if (indexList.isEmpty()) return null
        if (currentPage < 1) return null
        offset = indexList[--currentPage - 1] ?: 0
        return offset
    }

    fun next(): Int {
        currentPage++
        offset = if (currentPage < indexList.size) {
            indexList[currentPage] ?: 0
        } else {
            indexList[indexList.size - 1] ?: 0
        }
        return offset
    }

    fun addIndex(count: Int) {
        if (!indexList.containsKey(currentPage)) {
            if (indexList.isEmpty()) {
                indexList.put(currentPage++, 0)
                indexList.put(currentPage, count)
                return
            }
            indexList.put(currentPage, indexList[indexList.size - 1]?.plus(count) ?: 0)
        }
    }

    fun reduce() = currentPage--

    @SuppressLint("NotifyDataSetChanged")
    fun updateCandidates(data: FcitxEvent.CandidateListEvent.Data, page: Page? = null) {
        this.candidates = data.candidates
        this.total = data.total
        if (page == null) {
            this.offset = if (data.currentPage == -1) 0 else data.currentPage * data.total
            this.currentPage = 0
            this.indexList.clear()
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = candidates.size

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val ui = CandidateItemUi(parent.context, theme)
        ui.root.apply {
            minimumWidth = dp(10)
            setPaddingDp(0, 1, 0, 1)
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
            holder.ui.label.text = list[1]
            holder.ui.label.visibility = VISIBLE
        } else {
            holder.ui.label.visibility = GONE
        }
        holder.text = text
        holder.idx = offset + position
    }
}
