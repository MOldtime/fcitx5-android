/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2024 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.candidates.expanded

import android.view.Gravity.CENTER_VERTICAL
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.android.flexbox.FlexboxLayoutManager
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.candidates.CandidateItemUi
import org.fcitx.fcitx5.android.input.candidates.CandidateViewHolder
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.wrapContent

open class PagingCandidateViewAdapter(val theme: Theme) :
    PagingDataAdapter<String, CandidateViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.contentEquals(newItem)
            }
        }
    }

    var offset = 0
        private set

    fun refreshWithOffset(offset: Int) {
        this.offset = offset
        refresh()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val ui = CandidateItemUi(parent.context, theme)
        ui.root.apply {
            ui.comment.textSize = 10f
            ui.text.textSize = 20f
            addView(ui.comment)
            addView(ui.text)
            gravity = CENTER_VERTICAL
            layoutParams = FlexboxLayoutManager.LayoutParams(wrapContent, matchParent)
        }
        return CandidateViewHolder(ui)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val text = getItem(position)!!
        val list = text.split(Regex("\\s+"), 2)
        holder.ui.text.text = list[0]
        if (list.size > 1) {
            holder.ui.comment.text = list[1]
            holder.ui.comment.visibility = VISIBLE
        } else {
            holder.ui.comment.visibility = GONE
        }
        holder.text = list[0]

//        holder.ui.text.text = text
//        holder.text = text
        holder.idx = position + offset
    }
}
