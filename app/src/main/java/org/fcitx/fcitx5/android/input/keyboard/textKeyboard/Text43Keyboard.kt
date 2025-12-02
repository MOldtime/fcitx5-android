/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2025 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.keyboard.textKeyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.View
import androidx.annotation.Keep
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxKeyMapping
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.KeySym
import org.fcitx.fcitx5.android.data.prefs.AppPrefs
import org.fcitx.fcitx5.android.data.prefs.ManagedPreference
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.keyboard.BackspaceKey
import org.fcitx.fcitx5.android.input.keyboard.ImageKeyView
import org.fcitx.fcitx5.android.input.keyboard.KeyAction
import org.fcitx.fcitx5.android.input.keyboard.KeyActionListener
import org.fcitx.fcitx5.android.input.keyboard.KeyDef
import org.fcitx.fcitx5.android.input.keyboard.LanguageKey
import org.fcitx.fcitx5.android.input.keyboard.SpaceKey
import org.fcitx.fcitx5.android.input.keyboard.TextKeyView

@SuppressLint("ViewConstructor")
class Text43Keyboard(
    context: Context, theme: Theme
) : TextKeyboardBase(context, theme, Layout) {

    companion object {
        val Layout: List<List<KeyDef>> = listOf(
            listOf(
                // @formatter:off
                AlphabetStandardKey("1", "!", behavior = setOf(KeyDef.Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_1), KeyStates.Companion.Empty)), KeyDef.Behavior.Swipe(KeyAction.FcitxKeyAction(act = "!".uppercase(), default = false)))),
                AlphabetStandardKey("2", "@", behavior = setOf(KeyDef.Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_2), KeyStates.Companion.Empty)), KeyDef.Behavior.Swipe(KeyAction.FcitxKeyAction(act = "@".uppercase(), default = false)))),
                AlphabetStandardKey("3", "#", behavior = setOf(KeyDef.Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_3), KeyStates.Companion.Empty)), KeyDef.Behavior.Swipe(KeyAction.FcitxKeyAction(act = "#".uppercase(), default = false)))),
                AlphabetStandardKey("4", "$", behavior = setOf(KeyDef.Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_4), KeyStates.Companion.Empty)), KeyDef.Behavior.Swipe(KeyAction.FcitxKeyAction(act = "$".uppercase(), default = false)))),
                AlphabetStandardKey("5", "%", behavior = setOf(KeyDef.Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_5), KeyStates.Companion.Empty)), KeyDef.Behavior.Swipe(KeyAction.FcitxKeyAction(act = "%".uppercase(), default = false)))),
                AlphabetStandardKey("6", "^", behavior = setOf(KeyDef.Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_6), KeyStates.Companion.Empty)), KeyDef.Behavior.Swipe(KeyAction.FcitxKeyAction(act = "^".uppercase(), default = false)))),
                AlphabetStandardKey("7", "&", behavior = setOf(KeyDef.Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_7), KeyStates.Companion.Empty)), KeyDef.Behavior.Swipe(KeyAction.FcitxKeyAction(act = "&".uppercase(), default = false)))),
                AlphabetStandardKey("8", "*", behavior = setOf(KeyDef.Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_8), KeyStates.Companion.Empty)), KeyDef.Behavior.Swipe(KeyAction.FcitxKeyAction(act = "*".uppercase(), default = false)))),
                AlphabetStandardKey("9", "(", behavior = setOf(KeyDef.Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_9), KeyStates.Companion.Empty)), KeyDef.Behavior.Swipe(KeyAction.FcitxKeyAction(act = "(".uppercase(), default = false)))),
                AlphabetStandardKey("0", ")", behavior = setOf(KeyDef.Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_0), KeyStates.Companion.Empty)), KeyDef.Behavior.Swipe(KeyAction.FcitxKeyAction(act = ")".uppercase(), default = false))))
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
                AlphabetStandardKey("A", "\\"),
                AlphabetStandardKey("S", "|"),
                AlphabetStandardKey("D", "×"),
                AlphabetStandardKey("F", "÷"),
                AlphabetStandardKey("G", "←"),
                AlphabetStandardKey("H", "→"),
                AlphabetStandardKey("J", "<"),
                AlphabetStandardKey("K", ">"),
                AlphabetStandardKey("L", TextKeyboardManagement.formContext[9].component1()),
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
                    KeyDef.Appearance.ImageText(" ", src = R.drawable.ic_baseline_tag_faces_24, textSize = 16f, textStyle = Typeface.NORMAL, percentWidth = 0.15f, variant = KeyDef.Appearance.Variant.Alternative, viewId = R.id.button_number), setOf( KeyDef.Behavior.Press(KeyAction.LayoutSwitchAction("")), KeyDef.Behavior.Swipe(KeyAction.PickerSwitchAction())),
                    arrayOf(
                        KeyDef.Popup.Menu(arrayOf(
                                KeyDef.Popup.Menu.Item("Emoji", R.drawable.ic_baseline_tag_faces_24, KeyAction.PickerSwitchAction()),
                                KeyDef.Popup.Menu.Item("QuickPhrase", R.drawable.ic_baseline_format_quote_24, KeyAction.QuickPhraseAction),
                                KeyDef.Popup.Menu.Item("Unicode", R.drawable.ic_logo_unicode, KeyAction.UnicodeAction)
                            ))
                    )),
                // @formatter:on
                Symbol43KeyId(",", R.id.button_left),
                LanguageKey(),
                SpaceKey(),
                Symbol43KeyId(".", R.id.button_right),
                ReturnKey()
            )
        )
    }

    val lang: ImageKeyView by lazy { findViewById(R.id.button_lang) }

    val buttonLeft: TextKeyView by lazy { findViewById(R.id.button_left) }
    val buttonRight: TextKeyView by lazy { findViewById(R.id.button_right) }

    private val showLangSwitchKey = AppPrefs.getInstance().keyboard.showLangSwitchKey

    @Keep
    private val showLangSwitchKeyListener = ManagedPreference.OnChangeListener<Boolean> { _, v ->
        updateLangSwitchKey(v)
    }

    init {
        updateLangSwitchKey(showLangSwitchKey.getValue())
        showLangSwitchKey.registerOnChangeListener(showLangSwitchKeyListener)
        buttonLeft.mainText.text = buildString { append(",") }
        buttonRight.mainText.text = buildString { append(".") }
        val symbolKeyClickListener = { v: View ->
            // @formatter:off
            v as TextKeyView
            super.onAction(KeyAction.FcitxKeyAction(v.mainText.text.toString()), KeyActionListener.Source.Keyboard)
            // @formatter:on
        }
        buttonRight.setOnClickListener(symbolKeyClickListener)
        buttonLeft.setOnClickListener(symbolKeyClickListener)
    }

    override fun onPanelUpdate(status: Boolean) {
        buttonNumber.mainText.text = buildString { append(if (status) "Esc" else "?123") }
        buttonLeft.mainText.text = buildString { append(if (status) ";" else ",") }
        buttonRight.mainText.text = buildString { append(if (status) "'" else ".") }
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

    private fun updateLangSwitchKey(visible: Boolean) {
        lang.visibility = if (visible) VISIBLE else GONE
    }
}