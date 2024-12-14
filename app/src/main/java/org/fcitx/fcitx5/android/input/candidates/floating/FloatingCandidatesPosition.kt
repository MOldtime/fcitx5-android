/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2024 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.candidates.floating

import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.data.prefs.ManagedPreferenceEnum

enum class FloatingCandidatesPosition(override val stringRes: Int) : ManagedPreferenceEnum {
    TopLeft(R.string.top_left),
    TopRight(R.string.top_right),
    BottomLeft(R.string.bottom_left),
    BottomRight(R.string.bottom_right),
    Follow(R.string.floating_follow)
}