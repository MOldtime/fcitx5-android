/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2024 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.candidates.floating

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.DrawableRes
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxEvent
import org.fcitx.fcitx5.android.core.FcitxKeyMapping
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.KeySym
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.FcitxInputMethodService
import org.fcitx.fcitx5.android.utils.styledFloat
import splitties.dimensions.dp
import splitties.resources.drawable
import splitties.views.dsl.constraintlayout.before
import splitties.views.dsl.constraintlayout.centerVertically
import splitties.views.dsl.constraintlayout.constraintLayout
import splitties.views.dsl.constraintlayout.endOfParent
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.add
import splitties.views.dsl.core.imageView
import splitties.views.imageDrawable

class PaginationUi(override val ctx: Context, val theme: Theme) : Ui {

    private fun createIcon(@DrawableRes icon: Int) = imageView {
        imageTintList = ColorStateList.valueOf(theme.keyTextColor)
        imageDrawable = drawable(icon)
        scaleType = ImageView.ScaleType.CENTER_CROP
    }

    private val prevIcon = createIcon(R.drawable.ic_baseline_arrow_prev_24)
    private val nextIcon = createIcon(R.drawable.ic_baseline_arrow_next_24)

    private val disabledAlpha = styledFloat(android.R.attr.disabledAlpha)

    override val root = constraintLayout {
        val w = dp(20)
        val h = dp(20)
        add(nextIcon, lParams(w, h) {
            centerVertically()
            endOfParent()
        })
        add(prevIcon, lParams(w, h) {
            centerVertically()
            before(nextIcon)
        })
    }

    fun update(data: FcitxEvent.PagedCandidateEvent.Data, service: FcitxInputMethodService) {
        prevIcon.alpha = if (data.hasPrev) {
            prevIcon.setOnClickListener {
                service.postFcitxJob {
                    sendKey(
                        KeySym(FcitxKeyMapping.FcitxKey_Page_Up).sym,
                        KeyStates.Empty.states
                    )
                }
            }
            1f
        } else disabledAlpha
        nextIcon.alpha = if (data.hasNext) {
            nextIcon.setOnClickListener {
                service.postFcitxJob {
                    sendKey(
                        KeySym(FcitxKeyMapping.FcitxKey_Page_Down).sym,
                        KeyStates.Empty.states
                    )
                }
            }
            1f
        } else disabledAlpha

    }
}
