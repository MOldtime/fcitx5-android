/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map

class ReleaseViewModel : ViewModel() {
    val httpClient = CoroutinesHttpClient()
    val releases = ReleaseRepository(httpClient).getReleases().map { data ->
        data.map { release ->
            release
        }
    }.cachedIn(viewModelScope)
}