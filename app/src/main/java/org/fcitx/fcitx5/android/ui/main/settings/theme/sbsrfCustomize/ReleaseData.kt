/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

data class ReleaseData(
    val fileToken: String,
    val fileName: String,
    val fileVersion: String,
    val fileSize: Long,
) {
    fun convertSize(): String {
        return "${fileSize / 1024 / 1024} MB"
    }
}
