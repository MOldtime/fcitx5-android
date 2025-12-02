/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2025 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.keyboard.textKeyboard

import android.graphics.Typeface
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxKeyMapping
import org.fcitx.fcitx5.android.core.KeyState
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.KeySym
import org.fcitx.fcitx5.android.data.InputFeedbacks
import org.fcitx.fcitx5.android.input.keyboard.BaseKeyboard
import org.fcitx.fcitx5.android.input.keyboard.KeyAction
import org.fcitx.fcitx5.android.input.keyboard.KeyDef
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance.Border
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance.Variant
import org.fcitx.fcitx5.android.input.keyboard.NumLockState

// Do not inherit the original method to avoid upstream updates affecting the actual effect

class Alphabet26Key(
    val character: String,
    val punctuation: String,
    variant: Variant = Variant.Normal,
    popup: Array<Popup>? = null
) : KeyDef(
    Appearance.AltText(
        displayText = character,
        altText = punctuation,
        textSize = 23f,
        variant = variant
    ),
    setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(character)),
        Behavior.Swipe(KeyAction.FcitxKeyAction(punctuation))
    ),
    popup ?: arrayOf(
        Popup.AltPreview(character, punctuation),
        Popup.Keyboard(character)
    )
)

class Symbol26Key(
    val symbol: String,
    viewId: Int,
    percentWidth: Float = 0.14f,
    variant: Variant = Variant.Normal,
    behaviors: Set<Behavior>? = null,
) : KeyDef(
    Appearance.Text(
        displayText = " ",
        textSize = 23f,
        percentWidth = percentWidth,
        variant = variant,
        viewId = viewId
    ),
    behaviors ?: setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(symbol))
    ),
)

class Alphabet35Key(
    val character: String,
    val punctuation: String = " ",
    variant: Variant = Variant.Normal,
    behavior: Set<Behavior>? = null,
    popup: Array<Popup>? = null,
    percentWidth: Float = 0.14f,
) : KeyDef(
    Appearance.AltText(
        percentWidth = percentWidth,
        textStyle = Typeface.NORMAL,
        displayText = character,
        altText = punctuation,
        textSize = 23f,
        variant = variant
    ),
    behavior ?: setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(character)),
        Behavior.SwipeCustomize(
            KeyAction.FcitxKeyAction(
                act = character.uppercase(),
                default = false
            ), BaseKeyboard.Swipe.Up
        ),
        Behavior.SwipeCustomize(
            KeyAction.FcitxKeyAction(
                act = punctuation.toString().uppercase(),
                default = false
            ), BaseKeyboard.Swipe.Down
        ),
    ),
    popup ?: arrayOf(
        Popup.AltPreview(character, punctuation),
        Popup.Keyboard(character)
    )
)

class Symbol35Key(
    val character: String,
    val punctuation: String,
    variant: Variant = Variant.Normal,
    popup: Array<Popup>? = null
) : KeyDef(
    Appearance.AltText(
        percentWidth = 0.1f,
        displayText = character,
        altText = punctuation,
        textSize = 23f,
        variant = variant
    ),
    setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(character)),
        Behavior.Swipe(KeyAction.FcitxKeyAction(punctuation))
    ),
    popup ?: arrayOf(
        Popup.AltPreview(character, punctuation),
        Popup.Keyboard(character)
    )
)

class Symbol43KeyId(
    val symbol: String,
    viewId: Int,
    percentWidth: Float = 0.15f,
    variant: Variant = Variant.Alternative,
    popup: Array<Popup>? = null
) : KeyDef(
    Appearance.Text(
        displayText = symbol,
        textSize = 23f,
        percentWidth = percentWidth,
        variant = variant,
        viewId = viewId
    ),
    setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(symbol)),
        Behavior.SwipeCustomize(KeyAction.FcitxKeyAction(symbol), BaseKeyboard.Swipe.Up),
        Behavior.SwipeCustomize(KeyAction.FcitxKeyAction(symbol), BaseKeyboard.Swipe.Down),
    ),
    popup ?: arrayOf(
        Popup.Preview(symbol),
        Popup.Keyboard(symbol)
    )
)

class NumPadKey(
    displayText: String,
    val sym: Int,
    textSize: Float = 16f,
    percentWidth: Float = 0.1f,
    variant: Variant = Variant.Normal,
    behaviors: Set<Behavior>? = null,
) : KeyDef(
    Appearance.Text(
        displayText,
        textSize = textSize,
        percentWidth = percentWidth,
        variant = variant
    ),
    behaviors ?: setOf(
        Behavior.Press(KeyAction.SymAction(KeySym(sym), NumLockState))
    )
)

class AlphabetStandardKey(
    val character: String,
    val punctuation: String = " ",
    variant: Variant = Variant.Normal,
    behavior: Set<Behavior>? = null,
    popup: Array<Popup>? = null,
    percentWidth: Float = 0.1f,
) : KeyDef(
    Appearance.AltText(
        percentWidth = percentWidth,
        textStyle = Typeface.NORMAL,
        displayText = character,
        altText = punctuation,
        textSize = 23f,
        variant = variant
    ),
    behavior ?: setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(character)),
        Behavior.Swipe(KeyAction.FcitxKeyAction(act = character.uppercase(), default = false)),
    ),
    popup ?: arrayOf(
        Popup.AltPreview(character, punctuation),
        Popup.Keyboard(character)
    )
)

class SymbolStandardKey(
    val character: String,
    val punctuation: String,
    variant: Variant = Variant.Normal,
    popup: Array<Popup>? = null
) : KeyDef(
    Appearance.AltText(
        percentWidth = 0.11f,
        displayText = character,
        altText = punctuation,
        textSize = 23f,
        variant = variant
    ),
    setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(character)),
        Behavior.Swipe(KeyAction.FcitxKeyAction(punctuation))
    ),
    popup ?: arrayOf(
        Popup.AltPreview(character, punctuation),
        Popup.Keyboard(character)
    )
)

class CapsKey : KeyDef(
    Appearance.Image(
        src = R.drawable.ic_capslock_none,
        viewId = R.id.button_caps,
        percentWidth = 0.15f,
        variant = Variant.Alternative
    ),
    setOf(
        // @formatter:off
        Behavior.SwipeCustomize(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_Tab), KeyStates(KeyState.Shift)), BaseKeyboard.Swipe.Up),
        Behavior.SwipeCustomize(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_Page_Down), KeyStates.Empty), BaseKeyboard.Swipe.Down),
        // @formatter:On
        Behavior.Press(KeyAction.CapsAction(false)),
        Behavior.LongPress(KeyAction.LangSwitchAction),
        Behavior.DoubleTap(KeyAction.CapsAction(true))
    )
)

class ReturnKey(percentWidth: Float = 0.15f) : KeyDef(
    Appearance.Image(
        src = R.drawable.ic_baseline_keyboard_return_24,
        percentWidth = percentWidth,
        variant = Variant.Accent,
        border = Border.Special,
        viewId = R.id.button_return,
        soundEffect = InputFeedbacks.SoundEffect.Return
    ),
    setOf(
        Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_Return))),
        Behavior.LongPress(
            KeyAction.SymAction(
                KeySym(FcitxKeyMapping.FcitxKey_Return),
                KeyStates(KeyState.Shift)
            )
        ),
        Behavior.Swipe(
            KeyAction.SymAction(
                KeySym(FcitxKeyMapping.FcitxKey_Return),
                KeyStates(KeyState.Ctrl)
            )
        )
    ),
//    arrayOf(
//        Popup.Menu(
//            arrayOf(
//                Popup.Menu.Item(
//                    "Emoji", R.drawable.ic_baseline_tag_faces_24, KeyAction.PickerSwitchAction()
//                )
//            )
//        )
//    ),
)