/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.fcitx.fcitx5.android.data.prefs.AppPrefs

class ViewModel : ViewModel() {
    private val sbxlmVersion =
        MutableStateFlow(AppPrefs.getInstance().sbxlmVersion.getValue())
    val httpClient = CoroutinesHttpClient()
    fun getReleases(): Flow<PagingData<ReleaseData>> {
        return ReleaseRepository(httpClient).getReleases()
            .map { data ->
                data.map {
                    if (it.fileToken == sbxlmVersion.value) {
                        it.copy(status = DownloadState.Installed(it.fileToken))
                    } else {
                        it
                    }
                }
            }
            .cachedIn(viewModelScope)
            .combine(DownloadAndInstallService.statusEvent) { data, status ->
                data.map {
                    if (status is DownloadState.Installed && sbxlmVersion.value != status.fileToken) {
                        sbxlmVersion.value = status.fileToken
                        AppPrefs.getInstance().sbxlmVersion.setValue(status.fileToken)
                    }

                    if (it.fileToken == DownloadAndInstallService.fileToken) {
                        it.copy(status = status)
                    } else {
                        it
                    }
                }
            }
            .combine(sbxlmVersion) { data, version ->
                data.map {
                    if (it.fileToken == version) {
                        it.copy(status = DownloadState.Installed(it.fileToken))
                    } else if (it.status is DownloadState.Installed) {
                        it.copy(status = DownloadState.Idle)
                    } else {
                        it
                    }
                }
            }
    }
}