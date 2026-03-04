/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

sealed class DownloadState {
    object Idle : DownloadState()

    data class Downloading(
        val progress: Int
    ) : DownloadState()

    object Downloaded : DownloadState()

    data class Installed(val fileToken: String) : DownloadState()
}