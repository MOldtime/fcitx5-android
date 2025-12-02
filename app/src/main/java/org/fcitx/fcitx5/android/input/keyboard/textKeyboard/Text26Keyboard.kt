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
import org.fcitx.fcitx5.android.core.InputMethodEntry
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
class Text26Keyboard(
    context: Context, theme: Theme
) : TextKeyboardBase(context, theme, Layout) {

    companion object {
        val Layout: List<List<KeyDef>> = listOf(
            listOf(
                Alphabet26Key("Q", "1"),
                Alphabet26Key("W", "2"),
                Alphabet26Key("E", "3"),
                Alphabet26Key("R", "4"),
                Alphabet26Key("T", "5"),
                Alphabet26Key("Y", "6"),
                Alphabet26Key("U", "7"),
                Alphabet26Key("I", "8"),
                Alphabet26Key("O", "9"),
                Alphabet26Key("P", "0")
            ), listOf(
                Alphabet26Key("A", "@"),
                Alphabet26Key("S", "*"),
                Alphabet26Key("D", "+"),
                Alphabet26Key("F", "-"),
                Alphabet26Key("G", "="),
                Alphabet26Key("H", "/"),
                Alphabet26Key("J", "#"),
                Alphabet26Key("K", "("),
                Alphabet26Key("L", ")")
            ), listOf(
                CapsKey(),
                Alphabet26Key("Z", "'"),
                Alphabet26Key("X", ":"),
                Alphabet26Key("C", "\""),
                Alphabet26Key("V", "?"),
                Alphabet26Key("B", "!"),
                Alphabet26Key("N", "~"),
                Alphabet26Key("M", "\\"),
                BackspaceKey()
            ), listOf(
                // @formatter:off
                KeyDef(KeyDef.Appearance.ImageText( " ", src = R.drawable.ic_baseline_tag_faces_24, textSize = 16f, textStyle = Typeface.NORMAL, percentWidth = 0.15f, variant = KeyDef.Appearance.Variant.Alternative, viewId = R.id.button_number), setOf( KeyDef.Behavior.Press(KeyAction.LayoutSwitchAction("")), KeyDef.Behavior.Swipe(KeyAction.PickerSwitchAction()))),
                Symbol26Key( ",", R.id.button_left, variant = KeyDef.Appearance.Variant.Alternative, behaviors = setOf(
                    KeyDef.Behavior.Press(KeyAction.FcitxKeyAction(",")),
                    KeyDef.Behavior.SwipeCustomize(KeyAction.FcitxKeyAction(","), Swipe.Up),
                    KeyDef.Behavior.SwipeCustomize(KeyAction.FcitxKeyAction(","), Swipe.Down)
                )),
                LanguageKey(),
                SpaceKey(),
                Symbol26Key( ".", R.id.button_right, variant = KeyDef.Appearance.Variant.Alternative, behaviors = setOf(
                    KeyDef.Behavior.Press(KeyAction.FcitxKeyAction(".")),
                    KeyDef.Behavior.SwipeCustomize(KeyAction.FcitxKeyAction("."), Swipe.Up),
                    KeyDef.Behavior.SwipeCustomize(KeyAction.FcitxKeyAction("."), Swipe.Down)
                )),
                ReturnKey()
                // @formatter:on
            )
        )
    }

    val buttonLeft: TextKeyView by lazy { findViewById(R.id.button_left) }
    val buttonRight: TextKeyView by lazy { findViewById(R.id.button_right) }

    val lang: ImageKeyView by lazy { findViewById(R.id.button_lang) }

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
            // @formatter:off
            buttonNumber.popupMenu(KeyDef.Popup.Menu(arrayOf(
                KeyDef.Popup.Menu.Item("Emoji", R.drawable.ic_baseline_tag_faces_24, KeyAction.PickerSwitchAction()),
                KeyDef.Popup.Menu.Item("QuickPhrase", R.drawable.ic_baseline_format_quote_24, KeyAction.QuickPhraseAction),
                KeyDef.Popup.Menu.Item("Unicode", R.drawable.ic_logo_unicode, KeyAction.UnicodeAction)
            )))
            // @formatter:on
        }
    }

    override fun onInputMethodUpdate(ime: InputMethodEntry) {
        languageCode = ime.languageCode
        space.mainText.text = buildString {
            append(if (ime.label == "En") ime.name else ime.label)
            ime.subMode.run {
                name.ifEmpty {
                    label.ifEmpty { null }
                }
            }?.let {
                append(" $it")
            }
        }
        if (capsState != CapsState.None) {
            switchCapsState()
        } else {
            updateAlphabetKeys()
        }
    }

    private fun updateLangSwitchKey(visible: Boolean) {
        lang.visibility = if (visible) VISIBLE else GONE
    }
}