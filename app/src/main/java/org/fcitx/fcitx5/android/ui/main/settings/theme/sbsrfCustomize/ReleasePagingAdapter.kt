/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ReleasePagingAdapter :
    PagingDataAdapter<ReleaseData, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<ReleaseData>() {
        override fun areItemsTheSame(
            oldItem: ReleaseData, newItem: ReleaseData
        ): Boolean {
            return oldItem.fileToken == newItem.fileToken
        }

        override fun areContentsTheSame(
            oldItem: ReleaseData, newItem: ReleaseData
        ): Boolean {
            return oldItem == newItem
        }
    }) {

    var onClick: (ItemUI.(data: ReleaseData, position: Int) -> Unit)? = null
    var onLongClick: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemUI = ItemUI(parent.context)
        return ReleaseViewHolder(itemUI)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null && onClick != null && onLongClick != null) {
            (holder.itemView as ItemUI).bindData(
                position,
                item,
                onClick!!,
                onLongClick!!
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any?>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }
        val value = payloads[0]
        if (value is DownloadState) {
            (holder.itemView as ItemUI).setDownloadButtonStatus(value)
        }
    }

    class ReleaseViewHolder(itemView: ItemUI) : RecyclerView.ViewHolder(itemView)
}