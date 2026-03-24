/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

import androidx.paging.PagingSource
import androidx.paging.PagingState

class ReleasePagingSource(private val client: CoroutinesHttpClient) :
    PagingSource<String, ReleaseData>() {

    companion object {
        private const val DEFAULT_PAGE_TOKEN = ""
    }

    override fun getRefreshKey(state: PagingState<String, ReleaseData>): String {
        return DEFAULT_PAGE_TOKEN
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ReleaseData> {
        val releases = mutableListOf<ReleaseData>()
        return client.getReleases(params.key ?: DEFAULT_PAGE_TOKEN, params.loadSize)?.let {
            if (it.code == 0) {
                it.data.items.forEach { item ->
                    releases.add(
                        ReleaseData(
                            item.fileToken,
                            item.assetName,
                            item.convertVersion(),
                            item.size,
                            item.preRelease,
                            DownloadState.Idle
                        )
                    )
                }
                LoadResult.Page(
                    data = releases,
                    prevKey = params.key,
                    nextKey = if (it.data.hasMore) it.data.pageToken else null
                )
            } else {
                LoadResult.Page(
                    data = releases,
                    prevKey = params.key,
                    nextKey = null
                )
            }
        } ?: LoadResult.Page(
            data = releases,
            prevKey = params.key,
            nextKey = null
        )
    }
}