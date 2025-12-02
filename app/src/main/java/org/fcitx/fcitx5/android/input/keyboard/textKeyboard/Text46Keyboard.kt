/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2023 Fcitx5 for Android Contributors
 */
package org.fcitx.fcitx5.android.input.keyboard.textKeyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxKeyMapping
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.KeySym
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.keyboard.BackspaceKey
import org.fcitx.fcitx5.android.input.keyboard.KeyAction
import org.fcitx.fcitx5.android.input.keyboard.KeyDef
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance.Variant
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Behavior
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Popup
import org.fcitx.fcitx5.android.input.keyboard.SpaceKey

@SuppressLint("ViewConstructor")
class Text46Keyboard(
    context: Context, theme: Theme
) : TextKeyboardBase(context, theme, Layout) {

    companion object {
        val Layout: List<List<KeyDef>> = listOf(
            listOf(
                // @formatter:off
                AlphabetStandardKey("1", "!", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_1), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "!".uppercase(), default = false)))),
                AlphabetStandardKey("2", "@", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_2), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "@".uppercase(), default = false)))),
                AlphabetStandardKey("3", "#", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_3), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "#".uppercase(), default = false)))),
                AlphabetStandardKey("4", "$", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_4), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "$".uppercase(), default = false)))),
                AlphabetStandardKey("5", "%", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_5), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "%".uppercase(), default = false)))),
                AlphabetStandardKey("6", "^", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_6), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "^".uppercase(), default = false)))),
                AlphabetStandardKey("7", "&", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_7), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "&".uppercase(), default = false)))),
                AlphabetStandardKey("8", "*", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_8), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "*".uppercase(), default = false)))),
                AlphabetStandardKey("9", "(", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_9), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "(".uppercase(), default = false)))),
                AlphabetStandardKey("0", ")", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_0), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = ")".uppercase(), default = false))))
                // @formatter:on
            ), listOf(
                AlphabetStandardKey("Q", "`"),
                AlphabetStandardKey("W", "~"),
                AlphabetStandardKey("E", "+"),
                AlphabetStandardKey("R", "-"),
                AlphabetStandardKey("T", "="),
                AlphabetStandardKey("Y", "_"),
                AlphabetStandardKey("U", "{"),
                AlphabetStandardKey("I", "}"),
                AlphabetStandardKey("O", "["),
                AlphabetStandardKey("P", "]")
            ), listOf(
                AlphabetStandardKey("A", "\\", percentWidth = 0.095f),
                AlphabetStandardKey("S", "|", percentWidth = 0.095f),
                AlphabetStandardKey("D", "×", percentWidth = 0.095f),
                AlphabetStandardKey("F", "÷", percentWidth = 0.095f),
                AlphabetStandardKey("G", "←", percentWidth = 0.095f),
                AlphabetStandardKey("H", "→", percentWidth = 0.095f),
                // @formatter:off
                AlphabetStandardKey("J", TextKeyboardManagement.formContext[7].component1(), percentWidth = 0.095f),
                AlphabetStandardKey("K", TextKeyboardManagement.formContext[8].component1(), percentWidth = 0.095f),
                AlphabetStandardKey("L", TextKeyboardManagement.formContext[9].component1(), percentWidth = 0.095f),
                AlphabetStandardKey(";", ":", percentWidth = 0.095f, behavior = setOf(
                        Behavior.Press(KeyAction.FcitxKeyAction(";")),
                        Behavior.Swipe(KeyAction.FcitxKeyAction(":"))
                    )
                ),
                // @formatter:on
            ), listOf(
                CapsKey(),
                AlphabetStandardKey("Z", TextKeyboardManagement.formContext[0].component1()),
                AlphabetStandardKey("X", TextKeyboardManagement.formContext[1].component1()),
                AlphabetStandardKey("C", TextKeyboardManagement.formContext[2].component1()),
                AlphabetStandardKey("V", TextKeyboardManagement.formContext[3].component1()),
                AlphabetStandardKey("B", TextKeyboardManagement.formContext[4].component1()),
                AlphabetStandardKey("N", TextKeyboardManagement.formContext[5].component1()),
                AlphabetStandardKey("M", TextKeyboardManagement.formContext[6].component1()),
                BackspaceKey()
            ), listOf(
                // @formatter:off
                KeyDef(
                    Appearance.ImageText(" ", src = R.drawable.ic_baseline_tag_faces_24, textSize = 16f, textStyle = Typeface.NORMAL, percentWidth = 0.15f, variant = Variant.Alternative, viewId = R.id.button_number), setOf( Behavior.Press(KeyAction.LayoutSwitchAction("")), Behavior.Swipe(KeyAction.PickerSwitchAction())),
                    arrayOf(
                        Popup.Menu(arrayOf(
                            Popup.Menu.Item("Emoji", R.drawable.ic_baseline_tag_faces_24, KeyAction.PickerSwitchAction()),
                            Popup.Menu.Item("QuickPhrase", R.drawable.ic_baseline_format_quote_24, KeyAction.QuickPhraseAction),
                            Popup.Menu.Item("Unicode", R.drawable.ic_logo_unicode, KeyAction.UnicodeAction)
                        ))
                    )),
                // @formatter:off
                SymbolStandardKey("/", "?", Variant.Alternative),
                SymbolStandardKey(",", "<", Variant.Alternative),
                SpaceKey(),
                SymbolStandardKey(".", ">", Variant.Alternative),
                SymbolStandardKey("'", "\"", Variant.Alternative),
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
        }
    }
}
