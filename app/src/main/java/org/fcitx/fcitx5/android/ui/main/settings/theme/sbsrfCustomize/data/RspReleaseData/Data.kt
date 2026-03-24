/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize.data.RspReleaseData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("has_more")
    val hasMore: Boolean,
    val items: List<Item>,
    @SerialName("page_token")
    val pageToken: String,
    val total: Int
)