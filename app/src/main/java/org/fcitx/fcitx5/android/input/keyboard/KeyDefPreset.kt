/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2023 Fcitx5 for Android Contributors
 */
package org.fcitx.fcitx5.android.input.keyboard

import android.graphics.Typeface
import androidx.annotation.DrawableRes
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxKeyMapping
import org.fcitx.fcitx5.android.core.KeyState
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.KeySym
import org.fcitx.fcitx5.android.data.InputFeedbacks
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance.Border
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance.Variant
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Behavior
import org.fcitx.fcitx5.android.input.picker.PickerWindow

val NumLockState = KeyStates(KeyState.NumLock, KeyState.Virtual)

class SymbolKey(
    val symbol: String,
    percentWidth: Float = 0.1f,
    variant: Variant = Variant.Normal,
    popup: Array<Popup>? = null
) : KeyDef(
    Appearance.Text(
        displayText = symbol,
        textSize = 23f,
        percentWidth = percentWidth,
        variant = variant
    ),
    setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(symbol))
    ),
    popup ?: arrayOf(
        Popup.Preview(symbol),
        Popup.Keyboard(symbol)
    )
)

class SymbolKeyId(
    val symbol: String,
    viewId: Int,
    percentWidth: Float = 0.14f,
    variant: Variant = Variant.Normal,
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
        Behavior.Press(KeyAction.FcitxKeyAction(symbol))
    ),
    popup ?: arrayOf(
        Popup.Preview(symbol),
        Popup.Keyboard(symbol)
    )
)

class SymbolKeyAlt(
    val symbol: String,
    altText: String,
    viewId: Int = -1,
    variant: Variant = Variant.Normal,
    popup: Array<Popup>? = null
) : KeyDef(
    Appearance.AltText(
        displayText = symbol,
        altText = altText,
        textSize = 23f,
        variant = variant,
        viewId = viewId
    ),
    setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(symbol)),
        Behavior.Swipe(KeyAction.FcitxKeyAction(altText))
    ),
    popup ?: arrayOf(
        Popup.Preview(symbol),
        Popup.Keyboard(symbol)
    )
)

class AlphabetKey(
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

class AlphabetTextKey(
    val character: String,
    variant: Variant = Variant.Normal,
    popup: Array<Popup>? = null
) : KeyDef(
    Appearance.AltText(
        displayText = character,
        altText = " ",
        textSize = 23f,
        variant = variant
    ),
    setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(character)),
    ),
    popup ?: arrayOf(
        Popup.Keyboard(character)
    )
)

class AlphabetKeyNew(
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

class AlphabetDigitKey(
    val character: String,
    altText: String,
    val sym: Int,
    popup: Array<Popup>? = null
) : KeyDef(
    Appearance.AltText(
        displayText = character,
        altText = altText,
        textSize = 23f
    ),
    setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(character)),
        Behavior.Swipe(KeyAction.SymAction(KeySym(sym), NumLockState))
    ),
    popup ?: arrayOf(
        Popup.AltPreview(character, altText),
        Popup.Keyboard(character)
    )
) {
    constructor(
        char: String,
        digit: Int,
        popup: Array<Popup>? = null
    ) : this(
        char,
        digit.toString(),
        FcitxKeyMapping.FcitxKey_KP_0 + digit,
        popup
    )
}

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

class LayoutSwitchKey(
    displayText: String,
    val to: String = "",
    percentWidth: Float = 0.15f,
    variant: Variant = Variant.Alternative
) : KeyDef(
    Appearance.Text(
        displayText,
        textSize = 16f,
        textStyle = Typeface.BOLD,
        percentWidth = percentWidth,
        variant = variant
    ),
    setOf(
        Behavior.Press(KeyAction.LayoutSwitchAction(to))
    )
)

class LayoutSwitchNumber(
    displayText: String,
    val to: String = "",
    percentWidth: Float = 0.15f,
    variant: Variant = Variant.Alternative
) : KeyDef(
    Appearance.Text(
        displayText,
        textSize = 16f,
        textStyle = Typeface.BOLD,
        percentWidth = percentWidth,
        variant = variant,
    ),
    setOf(
        Behavior.Press(KeyAction.LayoutSwitchAction(to)),
    ),
    arrayOf(
        Popup.Menu(
            arrayOf(
                Popup.Menu.Item(
                    "Emoji",
                    R.drawable.ic_baseline_tag_faces_24,
                    KeyAction.PickerSwitchAction()
                ),
                Popup.Menu.Item(
                    "QuickPhrase",
                    R.drawable.ic_baseline_format_quote_24,
                    KeyAction.QuickPhraseAction
                ),
                Popup.Menu.Item(
                    "Unicode",
                    R.drawable.ic_logo_unicode,
                    KeyAction.UnicodeAction
                )
            )
        )
    )
)

class BackspaceKey(
    percentWidth: Float = 0.15f,
    variant: Variant = Variant.Alternative
) : KeyDef(
    Appearance.Image(
        src = R.drawable.ic_baseline_backspace_24,
        percentWidth = percentWidth,
        variant = variant,
        viewId = R.id.button_backspace,
        soundEffect = InputFeedbacks.SoundEffect.Delete
    ),
    setOf(
        Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_BackSpace))),
        Behavior.Repeat(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_BackSpace)))
    )
)

class QuickPhraseKey : KeyDef(
    Appearance.Image(
        src = R.drawable.ic_baseline_format_quote_24,
        variant = Variant.Alternative,
        viewId = R.id.button_quickphrase
    ),
    setOf(
        Behavior.Press(KeyAction.QuickPhraseAction),
        Behavior.LongPress(KeyAction.UnicodeAction)
    )
)

class CommaKey(
    percentWidth: Float,
    variant: Variant,
) : KeyDef(
    Appearance.ImageText(
        displayText = ",",
        textSize = 23f,
        percentWidth = percentWidth,
        variant = variant,
        src = R.drawable.ic_baseline_tag_faces_24
    ),
    setOf(
        Behavior.Press(KeyAction.FcitxKeyAction(","))
    ),
    arrayOf(
        Popup.Preview(","),
        Popup.Menu(
            arrayOf(
                Popup.Menu.Item(
                    "Emoji",
                    R.drawable.ic_baseline_tag_faces_24,
                    KeyAction.PickerSwitchAction()
                ),
                Popup.Menu.Item(
                    "QuickPhrase",
                    R.drawable.ic_baseline_format_quote_24,
                    KeyAction.QuickPhraseAction
                ),
                Popup.Menu.Item(
                    "Unicode",
                    R.drawable.ic_logo_unicode,
                    KeyAction.UnicodeAction
                )
            )
        )
    )
)

class LanguageKey : KeyDef(
    Appearance.Image(
        src = R.drawable.ic_baseline_language_24,
        variant = Variant.AltForeground,
        viewId = R.id.button_lang
    ),
    setOf(
        Behavior.Press(KeyAction.LangSwitchAction),
        Behavior.LongPress(KeyAction.ShowInputMethodPickerAction)
    )
)

class Emoji : KeyDef(
    Appearance.ImageText(
        displayText = "?",
        textSize = 16f,
        percentWidth = 0.1f,
        variant = Variant.Alternative,
        src = R.drawable.ic_baseline_tag_faces_24
    ),
    setOf(
        Behavior.Press(KeyAction.FcitxKeyAction("?")),
        Behavior.Swipe(KeyAction.PickerSwitchAction()),
    ),
    arrayOf(
        Popup.Menu(
            arrayOf(
                Popup.Menu.Item(
                    "Emoji",
                    R.drawable.ic_baseline_tag_faces_24,
                    KeyAction.PickerSwitchAction()
                ),
                Popup.Menu.Item(
                    "QuickPhrase",
                    R.drawable.ic_baseline_format_quote_24,
                    KeyAction.QuickPhraseAction
                ),
                Popup.Menu.Item(
                    "Unicode",
                    R.drawable.ic_logo_unicode,
                    KeyAction.UnicodeAction
                )
            )
        )
    )
)

class SpaceKey : KeyDef(
    Appearance.Text(
        displayText = " ",
        textSize = 13f,
        percentWidth = 0f,
        border = Border.Special,
        viewId = R.id.button_space,
        soundEffect = InputFeedbacks.SoundEffect.SpaceBar
    ),
    setOf(
        Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_space))),
        Behavior.LongPress(KeyAction.SpaceLongPressAction),
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

class ImageLayoutSwitchKey(
    @DrawableRes
    icon: Int,
    to: String,
    percentWidth: Float = 0.1f,
    variant: Variant = Variant.AltForeground,
    viewId: Int = -1
) : KeyDef(
    Appearance.Image(
        src = icon,
        percentWidth = percentWidth,
        variant = variant,
        viewId = viewId
    ),
    setOf(
        Behavior.Press(KeyAction.LayoutSwitchAction(to))
    )
)

class ImagePickerSwitchKey(
    @DrawableRes
    icon: Int,
    to: PickerWindow.Key,
    percentWidth: Float = 0.1f,
    variant: Variant = Variant.AltForeground,
    viewId: Int = -1
) : KeyDef(
    Appearance.Image(
        src = icon,
        percentWidth = percentWidth,
        variant = variant,
        viewId = viewId
    ),
    setOf(
        Behavior.Press(KeyAction.PickerSwitchAction(to))
    )
)

class TextPickerSwitchKey(
    text: String,
    to: PickerWindow.Key,
    percentWidth: Float = 0.1f,
    variant: Variant = Variant.AltForeground,
    viewId: Int = -1
) : KeyDef(
    Appearance.Text(
        displayText = text,
        textSize = 16f,
        percentWidth = percentWidth,
        variant = variant,
        viewId = viewId,
        textStyle = Typeface.BOLD
    ),
    setOf(
        Behavior.Press(KeyAction.PickerSwitchAction(to))
    )
)

class MiniSpaceKey : KeyDef(
    Appearance.Image(
        src = R.drawable.ic_baseline_space_bar_24,
        percentWidth = 0.15f,
        variant = Variant.Alternative,
        viewId = R.id.button_mini_space
    ),
    setOf(
        Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_space)))
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
