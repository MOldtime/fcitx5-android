/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class ReleaseRepository(private val client: CoroutinesHttpClient) {
    fun getReleases(): Flow<PagingData<ReleaseData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,           // 每页大小
                enablePlaceholders = true, // 是否启用占位符
                initialLoadSize = 20,    // 初始加载大小
                maxSize = 50,          // 最大缓存大小
                prefetchDistance = 5   // 预加载距离
            ),
            pagingSourceFactory = { ReleasePagingSource(client) }
        ).flow
    }
}