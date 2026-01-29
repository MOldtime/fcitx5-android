/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize.data.RspReleaseData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class Item(
    @SerialName("file_token")
    val fileToken: String,
    @SerialName("release_name")
    val releaseName: String,
    @SerialName("release_id")
    val releaseID: String,
    @SerialName("asset_name")
    val assetName: String,
    @SerialName("asset_id")
    val assetID: String,
    @SerialName("pre_release")
    val preRelease: Boolean,
    @SerialName("create_time")
    val createTime: Long,
    val size: Long
) {
    fun convertVersion(): String {
        return "声笔 " + Instant.ofEpochMilli(createTime)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}