/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize.data.RspReleaseData

import kotlinx.serialization.Serializable
import org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize.data.baseData

@Serializable
data class RspReleaseData(
    override val code: Int,
    override val msg: String,
    val `data`: Data,
) : baseData