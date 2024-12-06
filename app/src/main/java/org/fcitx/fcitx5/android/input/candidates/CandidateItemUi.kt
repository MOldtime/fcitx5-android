/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2023 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.candidates

import android.content.Context
import android.view.View.GONE
import android.widget.LinearLayout
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.AutoScaleTextView
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.view
import splitties.views.gravityCenter

class CandidateItemUi(override val ctx: Context, theme: Theme) : Ui {

    val text = view(::AutoScaleTextView) {
        scaleMode = AutoScaleTextView.Mode.None
        isSingleLine = true
        gravity = gravityCenter
        setTextColor(theme.keyTextColor)
    }

    val comment = view(::AutoScaleTextView) {
        visibility = GONE
        scaleMode = AutoScaleTextView.Mode.Proportional
        isSingleLine = true
        gravity = gravityCenter
        setTextColor(theme.altKeyTextColor)
    }

    override val root = LinearLayout(ctx).apply {
        orientation = LinearLayout.VERTICAL
    }
    /** 原生
    override val root = view(::CustomGestureView) {
    background = pressHighlightDrawable(theme.keyPressHighlightColor)

    /**
     * candidate long press feedback is handled by [org.fcitx.fcitx5.android.input.candidates.horizontal.HorizontalCandidateComponent.showCandidateActionMenu]
    */
    longPressFeedbackEnabled = false

    add(text, lParams(wrapContent, matchParent) {
    gravity = gravityTop
    })
     **/
}
