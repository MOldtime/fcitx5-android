/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2025 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.keyboard.textKeyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxKeyMapping
import org.fcitx.fcitx5.android.core.InputMethodEntry
import org.fcitx.fcitx5.android.core.KeyState
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.KeySym
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.keyboard.BackspaceKey
import org.fcitx.fcitx5.android.input.keyboard.KeyAction
import org.fcitx.fcitx5.android.input.keyboard.KeyDef
import org.fcitx.fcitx5.android.input.keyboard.SpaceKey

@SuppressLint("ViewConstructor")
class Text35Keyboard(
    context: Context, theme: Theme
) : TextKeyboardBase(context, theme, Layout) {

    companion object {
        val Layout: List<List<KeyDef>> = listOf(
            listOf(
                Alphabet35Key("Q", "`"),
                Alphabet35Key("W", "!"),
                Alphabet35Key("R", "@"),
                Alphabet35Key("T", "#"),
                Alphabet35Key("Y", "$"),
                Alphabet35Key("L", "%"),
                Alphabet35Key("P", "_")
            ), listOf(
                Alphabet35Key("S", "|"),
                Alphabet35Key("D", "^"),
                Alphabet35Key("F", "&"),
                Alphabet35Key("G", "*"),
                Alphabet35Key("H", "("),
                Alphabet35Key("J", ")"),
                Alphabet35Key("K", "=")
            ), listOf(
                Alphabet35Key("Z", "<"),
                Alphabet35Key("X", ">"),
                Alphabet35Key("C", "{"),
                Alphabet35Key("V", "}"),
                Alphabet35Key("B", "["),
                Alphabet35Key("N", "]"),
                Alphabet35Key("M", "·"),
            ), listOf(
                // @formatter:off
                CapsKey(),
                Alphabet35Key("A", behavior = setOf(KeyDef.Behavior.Press(KeyAction.FcitxKeyAction("A")), KeyDef.Behavior.SwipeCustomize( KeyAction.FcitxKeyAction( act = "A".uppercase(), default = false), Swipe.Up))),
                Alphabet35Key("E", TextKeyboardManagement.formContext[9].component1(), behavior = setOf(KeyDef.Behavior.Press(KeyAction.FcitxKeyAction("E")), KeyDef.Behavior.SwipeCustomize( KeyAction.FcitxKeyAction( act = "E".uppercase(), default = false), Swipe.Up), KeyDef.Behavior.SwipeCustomize(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_l), KeyStates(KeyState.Ctrl)), Swipe.Down))),
                Alphabet35Key("U", "÷"),
                Alphabet35Key("I", TextKeyboardManagement.formContext[9].component1(), behavior = setOf(KeyDef.Behavior.Press(KeyAction.FcitxKeyAction("I")), KeyDef.Behavior.SwipeCustomize( KeyAction.FcitxKeyAction( act = "I".uppercase(), default = false), Swipe.Up), KeyDef.Behavior.SwipeCustomize(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_l), KeyStates(KeyState.Ctrl)), Swipe.Down))),
                Alphabet35Key("O", behavior = setOf(KeyDef.Behavior.Press(KeyAction.FcitxKeyAction("O")), KeyDef.Behavior.SwipeCustomize( KeyAction.FcitxKeyAction( act = "O".uppercase(), default = false), Swipe.Up))),
                BackspaceKey()
                // @formatter:on
            ), listOf(
                // @formatter:off
                KeyDef(
                    KeyDef.Appearance.ImageText(" ", src = R.drawable.ic_baseline_tag_faces_24, textSize = 16f, textStyle = Typeface.NORMAL, percentWidth = 0.15f, variant = KeyDef.Appearance.Variant.Alternative, viewId = R.id.button_number), setOf( KeyDef.Behavior.Press(KeyAction.LayoutSwitchAction("")), KeyDef.Behavior.Swipe(KeyAction.PickerSwitchAction())),
                    arrayOf(
                        KeyDef.Popup.Menu(arrayOf(
                            KeyDef.Popup.Menu.Item("Emoji", R.drawable.ic_baseline_tag_faces_24, KeyAction.PickerSwitchAction()),
                            KeyDef.Popup.Menu.Item("QuickPhrase", R.drawable.ic_baseline_format_quote_24, KeyAction.QuickPhraseAction),
                            KeyDef.Popup.Menu.Item("Unicode", R.drawable.ic_logo_unicode, KeyAction.UnicodeAction)
                        ))
                    )),
                // @formatter:on
                Symbol35Key(".", "?", KeyDef.Appearance.Variant.Alternative),
                Symbol35Key(",", "/", KeyDef.Appearance.Variant.Alternative),
                SpaceKey(),
                Symbol35Key(";", ":", KeyDef.Appearance.Variant.Alternative),
                Symbol35Key("'", "\"", KeyDef.Appearance.Variant.Alternative),
                ReturnKey()
            )
        )
    }

    override fun onPanelUpdate(status: Boolean) {
        buttonNumber.mainText.text = buildString { append(if (status) "Esc" else "?123") }
        `return`.swipeEnabled = status
        if (status) {
            buttonNumber.setOnClickListener {
                onAction(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_Escape)))
            }

            buttonNumber.setOnLongClickListener {
                onAction(KeyAction.LayoutSwitchAction(""))
                true
            }
        } else {
            buttonNumber.setOnClickListener {
                onAction(KeyAction.LayoutSwitchAction(""))
            }

            buttonNumber.popupMenu(
                KeyDef.Popup.Menu(
                    arrayOf(
                        KeyDef.Popup.Menu.Item(
                            "Emoji",
                            R.drawable.ic_baseline_tag_faces_24,
                            KeyAction.PickerSwitchAction()
                        ),
                        KeyDef.Popup.Menu.Item(
                            "QuickPhrase",
                            R.drawable.ic_baseline_format_quote_24,
                            KeyAction.QuickPhraseAction
                        ),
                        KeyDef.Popup.Menu.Item(
                            "Unicode",
                            R.drawable.ic_logo_unicode,
                            KeyAction.UnicodeAction
                        )
                    )
                )
            )
        }
    }

    override fun onInputMethodUpdate(ime: InputMethodEntry) {
        languageCode = ime.languageCode
        space.mainText.text = buildString {
            append(
                when (ime.label) {
                    "En" -> ime.name
                    "ㄓ" -> ""
                    else -> ime.label
                }
            )
            ime.subMode.run { name.ifEmpty { label.ifEmpty { null } } }?.let { append(" $it") }
        }
        if (capsState != CapsState.None) {
            switchCapsState()
        } else {
            updateAlphabetKeys()
        }
    }
}