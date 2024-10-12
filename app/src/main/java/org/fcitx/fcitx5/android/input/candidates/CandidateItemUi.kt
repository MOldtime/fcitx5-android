/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2023 Fcitx5 for Android Contributors
 */
package org.fcitx.fcitx5.android.input.candidates

import android.content.Context
import android.view.Gravity.CENTER_VERTICAL
import android.widget.LinearLayout
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.AutoScaleTextView
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.view
import splitties.views.gravityCenter

class CandidateItemUi(override val ctx: Context, theme: Theme) : Ui {

    val text = view(::AutoScaleTextView) {
        scaleMode = AutoScaleTextView.Mode.None
        textSize = 20f // sp
        isSingleLine = true
        gravity = gravityCenter
        setTextColor(theme.keyTextColor)
    }

    val label = view(::AutoScaleTextView) {
        scaleMode = AutoScaleTextView.Mode.Proportional
        textSize = 10f // sp
        isSingleLine = true
        gravity = gravityCenter
        setTextColor(theme.keyTextColor)
    }

    override val root = LinearLayout(ctx).apply {
        orientation = LinearLayout.VERTICAL
        gravity = CENTER_VERTICAL
        addView(label)
        addView(text)
    }
    /** 原生
    override val root = view(::CustomGestureView) {
    background = pressHighlightDrawable(theme.keyPressHighlightColor)

    /**
     * candidate long press feedback is handled by [HorizontalCandidateComponent.showCandidateActionMenu]
    */
    longPressFeedbackEnabled = false

    add(text, lParams(wrapContent, matchParent) {
    gravity = gravityTop
    })
    }
     **/
}