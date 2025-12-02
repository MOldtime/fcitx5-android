/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2025 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.keyboard.textKeyboard

import android.content.Context
import android.view.KeyEvent
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxKeyMapping
import org.fcitx.fcitx5.android.core.KeyState
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.KeySym
import org.fcitx.fcitx5.android.data.prefs.ManagedPreferenceEnum
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.data.theme.ThemeManager
import org.fcitx.fcitx5.android.input.clipboard.ClipboardWindow
import org.fcitx.fcitx5.android.input.keyboard.KeyAction

object TextKeyboardManagement {
    val formContext = listOf(
        "全选" to KeyAction.PerformContextMenuAction(android.R.id.selectAll),
        "剪切" to KeyAction.PerformContextMenuAction(android.R.id.cut),
        "复制" to KeyAction.PerformContextMenuAction(android.R.id.copy),
        "粘贴" to KeyAction.PerformContextMenuAction(android.R.id.paste),
        "剪贴" to KeyAction.attachWindow(ClipboardWindow()),
        "翻转" to KeyAction.SymAction(
            KeySym(FcitxKeyMapping.FcitxKey_Return),
            KeyStates(KeyState.Shift)
        ),
        "大写" to KeyAction.SymAction(
            KeySym(FcitxKeyMapping.FcitxKey_Return),
            KeyStates(KeyState.Ctrl)
        ),
        "↶" to KeyAction.sendCombinationKey(KeyEvent.KEYCODE_Z, ctrl = true),
        "↷" to KeyAction.sendCombinationKey(
            KeyEvent.KEYCODE_Z,
            ctrl = true,
            shift = true
        ),
        "⇐" to KeyAction.SymAction(
            KeySym(FcitxKeyMapping.FcitxKey_l),
            KeyStates(KeyState.Ctrl)
        )
    )

    enum class LayoutSelect(override val stringRes: Int) : ManagedPreferenceEnum {
        KeyLayout26(R.string.layout_select_26key) {
            override val keyboardName: String = "26KeyLayout"
        },
        KeyLayout35(R.string.layout_select_35key) {
            override val keyboardName: String = "35KeyLayout"
        },
        KeyLayout43(R.string.layout_select_43key) {
            override val keyboardName: String = "43KeyLayout"
        },
        KeyLayout45(R.string.layout_select_45key) {
            override val keyboardName = "45KeyLayout"
        },
        KeyLayout45NumberBelow(R.string.layout_select_45key_numbers_below) {
            override val keyboardName: String = "43KeyLayoutNumberBelow"
        },
        KeyLayout46(R.string.layout_select_46key) {
            override val keyboardName: String = "46KeyLayout"
        };

        abstract val keyboardName: String
    }


    fun getPairKeyboard(context: Context, theme: Theme): Pair<String, TextKeyboardBase> =
        getCurrentKeyboardName() to getKeyboard(context, theme)


    fun getKeyboard(context: Context, theme: Theme): TextKeyboardBase {
        return when (ThemeManager.prefs.layoutSelect.getValue()) {
            LayoutSelect.KeyLayout26 -> Text26Keyboard(context, theme)
            LayoutSelect.KeyLayout35 -> Text35Keyboard(context, theme)
            LayoutSelect.KeyLayout45 -> Text45Keyboard(context, theme)
            LayoutSelect.KeyLayout43 -> Text43Keyboard(context, theme)
            LayoutSelect.KeyLayout46 -> Text46Keyboard(context, theme)
            LayoutSelect.KeyLayout45NumberBelow -> Text45KeyboardNumberBelow(context, theme)
        }
    }

    fun getPopupPreset(): Map<String, Array<String>> {
        return when (ThemeManager.prefs.layoutSelect.getValue()) {
            LayoutSelect.KeyLayout26 -> {
                hashMapOf(
                    //
                    // Latin
                    //
                    "q" to arrayOf("1", "Q"),
                    "w" to arrayOf("2", "W"),
                    "e" to arrayOf("3", "E", "ê", "ë", "ē", "é", "ě", "è", "ė", "ę", "ȩ", "ḝ", "ə"),
                    "r" to arrayOf("4", "R"),
                    "t" to arrayOf("5", "T"),
                    "y" to arrayOf("6", "Y", "ÿ", "ұ", "ү", "ӯ", "ў"),
                    "u" to arrayOf("7", "U", "û", "ü", "ū", "ú", "ǔ", "ù"),
                    "i" to arrayOf("8", "I", "î", "ï", "ī", "í", "ǐ", "ì", "į", "ı"),
                    "o" to arrayOf("9", "O", "ô", "ö", "ō", "ó", "ǒ", "ò", "œ", "ø", "õ"),
                    "p" to arrayOf("0", "P"),
                    "a" to arrayOf("@", "A", "â", "ä", "ā", "á", "ǎ", "à", "æ", "ã", "å"),
                    "s" to arrayOf("*", "S", "ß", "ś", "š", "ş"),
                    "d" to arrayOf("+", "D", "ð"),
                    "f" to arrayOf("-", "F"),
                    "g" to arrayOf("=", "G", "ğ"),
                    "h" to arrayOf("/", "H"),
                    "j" to arrayOf("#", "J"),
                    "k" to arrayOf("(", "[", "{", "K"),
                    "l" to arrayOf(")", "]", "}", "L", "ł"),
                    "z" to arrayOf("'", "Z", "`", "ž", "ź", "ż"),
                    "x" to arrayOf(":", "X", "×"),
                    "c" to arrayOf("\"", "C", "ç", "ć", "č"),
                    "v" to arrayOf("?", "V", "¿", "ü", "ǖ", "ǘ", "ǚ", "ǜ"),
                    "b" to arrayOf("!", "B", "¡"),
                    "n" to arrayOf("~", "N", "ñ", "ń"),
                    "m" to arrayOf("\\", "M"),
                    //
                    // Upper case Latin
                    //
                    "Q" to arrayOf("1", "q"),
                    "W" to arrayOf("2", "w"),
                    "E" to arrayOf("3", "e", "Ê", "Ë", "Ē", "É", "È", "Ė", "Ę", "Ȩ", "Ḝ", "Ə"),
                    "R" to arrayOf("4", "r"),
                    "T" to arrayOf("5", "t"),
                    "Y" to arrayOf("6", "y", "Ÿ", "Ұ", "Ү", "Ӯ", "Ў"),
                    "U" to arrayOf("7", "u", "Û", "Ü", "Ù", "Ú", "Ū"),
                    "I" to arrayOf("8", "i", "Î", "Ï", "Í", "Ī", "Į", "Ì"),
                    "O" to arrayOf("9", "o", "Ô", "Ö", "Ò", "Ó", "Œ", "Ø", "Ō", "Õ"),
                    "P" to arrayOf("0", "p"),
                    "A" to arrayOf("@", "a", "Â", "Ä", "Ā", "Á", "À", "Æ", "Ã", "Å"),
                    "S" to arrayOf("*", "s", "ẞ", "Ś", "Š", "Ş"),
                    "D" to arrayOf("+", "d", "Ð"),
                    "F" to arrayOf("-", "f"),
                    "G" to arrayOf("=", "g", "Ğ"),
                    "H" to arrayOf("/", "h"),
                    "J" to arrayOf("#", "j"),
                    "K" to arrayOf("(", "k"),
                    "L" to arrayOf(")", "l", "Ł"),
                    "Z" to arrayOf("'", "z", "`", "Ž", "Ź", "Ż"),
                    "X" to arrayOf(":", "x"),
                    "C" to arrayOf("\"", "c", "Ç", "Ć", "Č"),
                    "V" to arrayOf("?", "v"),
                    "B" to arrayOf("!", "b", "¡"),
                    "N" to arrayOf("~", "n", "Ñ", "Ń"),
                    "M" to arrayOf("\\", "m"),
                    //
                    // Upper case Cyrillic
                    //
                    "г" to arrayOf("ғ"),
                    "е" to arrayOf("ё"),      // this in fact NOT the same E as before
                    "и" to arrayOf("ӣ", "і"), // і is not i
                    "й" to arrayOf("ј"),      // ј is not j
                    "к" to arrayOf("қ", "ҝ"),
                    "н" to arrayOf("ң", "һ"), // һ is not h
                    "о" to arrayOf("ә", "ө"),
                    "ч" to arrayOf("ҷ", "ҹ"),
                    "ь" to arrayOf("ъ"),
                    //
                    // Cyrillic
                    //
                    "Г" to arrayOf("Ғ"),
                    "Е" to arrayOf("Ё"),      // This In Fact Not The Same E As Before
                    "И" to arrayOf("Ӣ", "І"), // І is sot I
                    "Й" to arrayOf("Ј"),      // Ј is sot J
                    "К" to arrayOf("Қ", "Ҝ"),
                    "Н" to arrayOf("Ң", "Һ"), // Һ is not H
                    "О" to arrayOf("Ә", "Ө"),
                    "Ч" to arrayOf("Ҷ", "Ҹ"),
                    "Ь" to arrayOf("Ъ"),
                    //
                    // Arabic
                    //
                    // This renders weirdly in text editors, but is valid code.
                    "ا" to arrayOf("أ", "إ", "آ", "ء"),
                    "ب" to arrayOf("پ"),
                    "ج" to arrayOf("چ"),
                    "ز" to arrayOf("ژ"),
                    "ف" to arrayOf("ڤ"),
                    "ك" to arrayOf("گ"),
                    "ل" to arrayOf("لا"),
                    "ه" to arrayOf("ه"),
                    "و" to arrayOf("ؤ"),
                    //
                    // Hebrew
                    //
                    // Likewise, this will render oddly, but is still valid code.
                    "ג" to arrayOf("ג׳"),
                    "ז" to arrayOf("ז׳"),
                    "ח" to arrayOf("ח׳"),
                    "צ׳" to arrayOf("צ׳"),
                    "ת" to arrayOf("ת׳"),
                    "י" to arrayOf("ײַ"),
                    "י" to arrayOf("ײ"),
                    "ח" to arrayOf("ױ"),
                    "ו" to arrayOf("װ"),
                    //
                    // Numbers
                    //
                    "0" to arrayOf("∅", "ⁿ", "⁰"),
                    "1" to arrayOf("¹", "½", "⅓", "¼", "⅕", "⅙", "⅐", "⅛", "⅑", "⅒"),
                    "2" to arrayOf("²", "⅖", "⅔"),
                    "3" to arrayOf("³", "⅗", "¾", "⅜"),
                    "4" to arrayOf("⁴", "⅘", "⅝", "⅚"),
                    "5" to arrayOf("⁵", "⅝", "⅚"),
                    "6" to arrayOf("⁶"),
                    "7" to arrayOf("⁷", "⅞"),
                    "8" to arrayOf("⁸"),
                    "9" to arrayOf("⁹"),
                    //
                    // Punctuation
                    //
                    "." to arrayOf(",", "?", "!", ":", ";", "_", "%", "$", "^", "&"),
                    "-" to arrayOf("—", "–", "·"),
                    "?" to arrayOf("¿", "‽"),
                    "'" to arrayOf("‘", "’", "‚", "›", "‹"),
                    "!" to arrayOf("¡"),
                    "\"" to arrayOf("“", "”", "„", "»", "«"),
                    "/" to arrayOf("÷"),
                    "#" to arrayOf("№"),
                    "%" to arrayOf("‰", "℅"),
                    "^" to arrayOf("↑", "↓", "←", "→"),
                    "+" to arrayOf("±"),
                    "<" to arrayOf("≤", "«", "‹", "⟨"),
                    "=" to arrayOf("∞", "≠", "≈"),
                    ">" to arrayOf("≥", "»", "›", "⟩"),
                    "°" to arrayOf("′", "″", "‴"),
                    //
                    // Currency
                    //
                    "$" to arrayOf("¢", "€", "£", "¥", "₹", "₽", "₺", "₩", "₱", "₿"),
                )
            }
            LayoutSelect.KeyLayout35 -> {
                hashMapOf(
                    //
                    // Latin
                    //
                    "q" to arrayOf("`", "q", "Q"),
                    "w" to arrayOf("1", "w", "W"),
                    "e" to arrayOf(
                        "↓",
                        "e",
                        "E",
                        "ê",
                        "ë",
                        "ē",
                        "é",
                        "ě",
                        "è",
                        "ė",
                        "ę",
                        "ȩ",
                        "ḝ",
                        "ə"
                    ),
                    "r" to arrayOf("2", "r", "R"),
                    "t" to arrayOf("3", "t", "T"),
                    "y" to arrayOf("4", "y", "Y", "ÿ", "ұ", "ү", "ӯ", "ў"),
                    "u" to arrayOf("×", "u", "U", "û", "ü", "ū", "ú", "ǔ", "ù"),
                    "i" to arrayOf("←", "i", "I", "î", "ï", "ī", "í", "ǐ", "ì", "į", "ı"),
                    "o" to arrayOf("→", "o", "O", "ô", "ö", "ō", "ó", "ǒ", "ò", "œ", "ø", "õ"),
                    "p" to arrayOf("-", "p", "P"),
                    "a" to arrayOf("↑", "a", "A", "â", "ä", "ā", "á", "ǎ", "à", "æ", "ã", "å"),
                    "s" to arrayOf("\\", "s", "S", "ß", "ś", "š", "ş"),
                    "d" to arrayOf("6", "d", "D", "ð"),
                    "f" to arrayOf("7", "f", "F"),
                    "g" to arrayOf("8", "g", "G", "ğ"),
                    "h" to arrayOf("9", "h", "H"),
                    "j" to arrayOf("0", "j", "J"),
                    "k" to arrayOf("+", "k", "K"),
                    "l" to arrayOf("5", "l", "L", "ł"),
                    "z" to arrayOf(formContext[0].component1(), "Z", "z", "ž", "ź", "ż"),
                    "x" to arrayOf(formContext[1].component1(), "X", "x", "×"),
                    "c" to arrayOf(formContext[2].component1(), "C", "c", "ç", "ć", "č"),
                    "v" to arrayOf(
                        formContext[3].component1(),
                        "V",
                        "v",
                        "¿",
                        "ü",
                        "ǖ",
                        "ǘ",
                        "ǚ",
                        "ǜ"
                    ),
                    "b" to arrayOf(formContext[4].component1(), "B", "b", "¡"),
                    "n" to arrayOf(formContext[5].component1(), "N", "n", "ñ", "ń"),
                    "m" to arrayOf(formContext[6].component1(), "M", "m"),
                    //
                    // Upper case Latin
                    //
                    "Q" to arrayOf("`", "Q", "q"),
                    "W" to arrayOf("~", "W", "w"),
                    "E" to arrayOf("+", "e", "E", "Ê", "Ë", "Ē", "É", "È", "Ė", "Ę", "Ȩ", "Ḝ", "Ə"),
                    "R" to arrayOf("-", "r", "R"),
                    "T" to arrayOf("=", "t", "T"),
                    "Y" to arrayOf("_", "y", "Y", "Ÿ", "Ұ", "Ү", "Ӯ", "Ў"),
                    "U" to arrayOf("{", "u", "≤", "U", "Û", "Ü", "Ù", "Ú", "Ū"),
                    "I" to arrayOf("}", "i", "≥", "I", "Î", "Ï", "Í", "Ī", "Į", "Ì"),
                    "O" to arrayOf("[", "o", "O", "Ô", "Ö", "Ò", "Ó", "Œ", "Ø", "Ō", "Õ"),
                    "P" to arrayOf("]", "p", "P"),
                    "A" to arrayOf("\\", "a", "A", "Â", "Ä", "Ā", "Á", "À", "Æ", "Ã", "Å"),
                    "S" to arrayOf("|", "s", "S", "ẞ", "Ś", "Š", "Ş"),
                    "D" to arrayOf("×", "d", "D", "Ð"),
                    "F" to arrayOf("÷", "f", "F"),
                    "G" to arrayOf("←", "g", "G", "Ğ"),
                    "H" to arrayOf("→", "h", "H"),
                    "J" to arrayOf("↑", "j", "J"),
                    "K" to arrayOf("↓️️", "k", "K"),
                    "L" to arrayOf("/", "l", "L", "ł"),
                    "Z" to arrayOf("z", "Z", "`", "Ž", "Ź", "Ż"),
                    "X" to arrayOf("x", "X"),
                    "C" to arrayOf("c", "C", "Ç", "Ć", "Č"),
                    "V" to arrayOf("v", "V"),
                    "B" to arrayOf("b", "B", "¡"),
                    "N" to arrayOf("n", "N", "Ñ", "Ń"),
                    "M" to arrayOf("m", "M"),
                    //
                    // Upper case Cyrillic
                    //
                    "г" to arrayOf("ғ"),
                    "е" to arrayOf("ё"),      // this in fact NOT the same E as before
                    "и" to arrayOf("ӣ", "і"), // і is not i
                    "й" to arrayOf("ј"),      // ј is not j
                    "к" to arrayOf("қ", "ҝ"),
                    "н" to arrayOf("ң", "һ"), // һ is not h
                    "о" to arrayOf("ә", "ө"),
                    "ч" to arrayOf("ҷ", "ҹ"),
                    "ь" to arrayOf("ъ"),
                    //
                    // Cyrillic
                    //
                    "Г" to arrayOf("Ғ"),
                    "Е" to arrayOf("Ё"),      // This In Fact Not The Same E As Before
                    "И" to arrayOf("Ӣ", "І"), // І is sot I
                    "Й" to arrayOf("Ј"),      // Ј is sot J
                    "К" to arrayOf("Қ", "Ҝ"),
                    "Н" to arrayOf("Ң", "Һ"), // Һ is not H
                    "О" to arrayOf("Ә", "Ө"),
                    "Ч" to arrayOf("Ҷ", "Ҹ"),
                    "Ь" to arrayOf("Ъ"),
                    //
                    // Arabic
                    //
                    // This renders weirdly in text editors, but is valid code.
                    "ا" to arrayOf("أ", "إ", "آ", "ء"),
                    "ب" to arrayOf("پ"),
                    "ج" to arrayOf("چ"),
                    "ز" to arrayOf("ژ"),
                    "ف" to arrayOf("ڤ"),
                    "ك" to arrayOf("گ"),
                    "ل" to arrayOf("لا"),
                    "ه" to arrayOf("ه"),
                    "و" to arrayOf("ؤ"),
                    //
                    // Hebrew
                    //
                    // Likewise, this will render oddly, but is still valid code.
                    "ג" to arrayOf("ג׳"),
                    "ז" to arrayOf("ז׳"),
                    "ח" to arrayOf("ח׳"),
                    "צ׳" to arrayOf("צ׳"),
                    "ת" to arrayOf("ת׳"),
                    "י" to arrayOf("ײַ"),
                    "י" to arrayOf("ײ"),
                    "ח" to arrayOf("ױ"),
                    "ו" to arrayOf("װ"),
                    //
                    // Numbers
                    //
                    "1" to arrayOf("!", "1", "¹", "½", "⅓", "¼", "⅕", "⅙", "⅐", "⅛", "⅑", "⅒"),
                    "2" to arrayOf("@", "2", "²", "⅖", "⅔"),
                    "3" to arrayOf("#", "3", "³", "⅗", "¾", "⅜"),
                    "4" to arrayOf("$", "4", "⁴", "⅘", "⅝", "⅚"),
                    "5" to arrayOf("%", "5", "⁵", "⅝", "⅚"),
                    "6" to arrayOf("^", "6", "⁶"),
                    "7" to arrayOf("&", "7", "⁷", "⅞"),
                    "8" to arrayOf("*", "8", "⁸"),
                    "9" to arrayOf("(", "9", "⁹"),
                    "0" to arrayOf(")", "0", "∅", "ⁿ", "⁰"),

                    //
                    // Punctuation
                    //
                    "," to arrayOf("/", "?", "!", ":", ";", "_", "%", "$", "^", "&"),
                    "'" to arrayOf("\""),
                    "." to arrayOf("?", "¿", "‽", ",", "!", ":", ";", "_", "%", "$", "^", "&"),
                    "-" to arrayOf("—", "–", "·"),
                    "?" to arrayOf(),
                    "'" to arrayOf("\"", "‘", "’", "‚", "›", "‹"),
                    "!" to arrayOf("¡"),
                    "\"" to arrayOf("“", "”", "„", "»", "«"),
                    "/" to arrayOf("?", "÷"),
                    "#" to arrayOf("№"),
                    "%" to arrayOf("‰", "℅"),
                    "^" to arrayOf("↑", "↓", "←", "→"),
                    "+" to arrayOf("±"),
                    "<" to arrayOf("≤", "«", "‹", "⟨"),
                    "=" to arrayOf("∞", "≠", "≈"),
                    ">" to arrayOf("≥", "»", "›", "⟩"),
                    "°" to arrayOf("′", "″", "‴"),
                    ";" to arrayOf(":"),
                    //
                    // Currency
                    //
                    "$" to arrayOf("¢", "€", "£", "¥", "₹", "₽", "₺", "₩", "₱", "₿"),
                )
            }
            LayoutSelect.KeyLayout45 -> {
                hashMapOf(
                    //
                    // Latin
                    //
                    "q" to arrayOf("`", "q", "Q"),
                    "w" to arrayOf("~", "w", "W"),
                    "e" to arrayOf(
                        "+",
                        "e",
                        "E",
                        "ê",
                        "ë",
                        "ē",
                        "é",
                        "ě",
                        "è",
                        "ė",
                        "ę",
                        "ȩ",
                        "ḝ",
                        "ə"
                    ),
                    "r" to arrayOf("-", "r", "R"),
                    "t" to arrayOf("=", "t", "T"),
                    "y" to arrayOf("_", "y", "Y", "ÿ", "ұ", "ү", "ӯ", "ў"),
                    "u" to arrayOf("{", "u", "U", "û", "ü", "ū", "ú", "ǔ", "ù"),
                    "i" to arrayOf("}", "i", "I", "î", "ï", "ī", "í", "ǐ", "ì", "į", "ı"),
                    "o" to arrayOf("[", "o", "O", "ô", "ö", "ō", "ó", "ǒ", "ò", "œ", "ø", "õ"),
                    "p" to arrayOf("]", "p", "P"),
                    "a" to arrayOf("\\", "a", "A", "â", "ä", "ā", "á", "ǎ", "à", "æ", "ã", "å"),
                    "s" to arrayOf("|", "s", "S", "ß", "ś", "š", "ş"),
                    "d" to arrayOf("×", "d", "D", "ð"),
                    "f" to arrayOf("÷", "f", "F"),
                    "g" to arrayOf("←", "g", "G", "ğ"),
                    "h" to arrayOf("→", "h", "H"),
                    "j" to arrayOf("<", "j", "J"),
                    "k" to arrayOf(">", "k", "K"),
                    "l" to arrayOf(formContext[9].component1(), "/", "l", "L", "ł"),
                    "z" to arrayOf(formContext[0].component1(), "Z", "z", "ž", "ź", "ż"),
                    "x" to arrayOf(formContext[1].component1(), "X", "x", "×"),
                    "c" to arrayOf(formContext[2].component1(), "C", "c", "ç", "ć", "č"),
                    "v" to arrayOf(
                        formContext[3].component1(),
                        "V",
                        "v",
                        "¿",
                        "ü",
                        "ǖ",
                        "ǘ",
                        "ǚ",
                        "ǜ"
                    ),
                    "b" to arrayOf(formContext[4].component1(), "B", "b", "¡"),
                    "n" to arrayOf(formContext[5].component1(), "N", "n", "ñ", "ń"),
                    "m" to arrayOf(formContext[6].component1(), "M", "m"),
                    //
                    // Upper case Latin
                    //
                    "Q" to arrayOf("`", "Q", "q"),
                    "W" to arrayOf("~", "W", "w"),
                    "E" to arrayOf("+", "e", "E", "Ê", "Ë", "Ē", "É", "È", "Ė", "Ę", "Ȩ", "Ḝ", "Ə"),
                    "R" to arrayOf("-", "r", "R"),
                    "T" to arrayOf("=", "t", "T"),
                    "Y" to arrayOf("_", "y", "Y", "Ÿ", "Ұ", "Ү", "Ӯ", "Ў"),
                    "U" to arrayOf("{", "u", "≤", "U", "Û", "Ü", "Ù", "Ú", "Ū"),
                    "I" to arrayOf("}", "i", "≥", "I", "Î", "Ï", "Í", "Ī", "Į", "Ì"),
                    "O" to arrayOf("[", "o", "O", "Ô", "Ö", "Ò", "Ó", "Œ", "Ø", "Ō", "Õ"),
                    "P" to arrayOf("]", "p", "P"),
                    "A" to arrayOf("\\", "a", "A", "Â", "Ä", "Ā", "Á", "À", "Æ", "Ã", "Å"),
                    "S" to arrayOf("|", "s", "S", "ẞ", "Ś", "Š", "Ş"),
                    "D" to arrayOf("×", "d", "D", "Ð"),
                    "F" to arrayOf("÷", "f", "F"),
                    "G" to arrayOf("←", "g", "G", "Ğ"),
                    "H" to arrayOf("→", "h", "H"),
                    "J" to arrayOf("↑", "j", "J"),
                    "K" to arrayOf("↓️️", "k", "K"),
                    "L" to arrayOf("/", "l", "L", "ł"),
                    "Z" to arrayOf("z", "Z", "`", "Ž", "Ź", "Ż"),
                    "X" to arrayOf("x", "X"),
                    "C" to arrayOf("c", "C", "Ç", "Ć", "Č"),
                    "V" to arrayOf("v", "V"),
                    "B" to arrayOf("b", "B", "¡"),
                    "N" to arrayOf("n", "N", "Ñ", "Ń"),
                    "M" to arrayOf("m", "M"),
                    //
                    // Upper case Cyrillic
                    //
                    "г" to arrayOf("ғ"),
                    "е" to arrayOf("ё"),      // this in fact NOT the same E as before
                    "и" to arrayOf("ӣ", "і"), // і is not i
                    "й" to arrayOf("ј"),      // ј is not j
                    "к" to arrayOf("қ", "ҝ"),
                    "н" to arrayOf("ң", "һ"), // һ is not h
                    "о" to arrayOf("ә", "ө"),
                    "ч" to arrayOf("ҷ", "ҹ"),
                    "ь" to arrayOf("ъ"),
                    //
                    // Cyrillic
                    //
                    "Г" to arrayOf("Ғ"),
                    "Е" to arrayOf("Ё"),      // This In Fact Not The Same E As Before
                    "И" to arrayOf("Ӣ", "І"), // І is sot I
                    "Й" to arrayOf("Ј"),      // Ј is sot J
                    "К" to arrayOf("Қ", "Ҝ"),
                    "Н" to arrayOf("Ң", "Һ"), // Һ is not H
                    "О" to arrayOf("Ә", "Ө"),
                    "Ч" to arrayOf("Ҷ", "Ҹ"),
                    "Ь" to arrayOf("Ъ"),
                    //
                    // Arabic
                    //
                    // This renders weirdly in text editors, but is valid code.
                    "ا" to arrayOf("أ", "إ", "آ", "ء"),
                    "ب" to arrayOf("پ"),
                    "ج" to arrayOf("چ"),
                    "ز" to arrayOf("ژ"),
                    "ف" to arrayOf("ڤ"),
                    "ك" to arrayOf("گ"),
                    "ل" to arrayOf("لا"),
                    "ه" to arrayOf("ه"),
                    "و" to arrayOf("ؤ"),
                    //
                    // Hebrew
                    //
                    // Likewise, this will render oddly, but is still valid code.
                    "ג" to arrayOf("ג׳"),
                    "ז" to arrayOf("ז׳"),
                    "ח" to arrayOf("ח׳"),
                    "צ׳" to arrayOf("צ׳"),
                    "ת" to arrayOf("ת׳"),
                    "י" to arrayOf("ײַ"),
                    "י" to arrayOf("ײ"),
                    "ח" to arrayOf("ױ"),
                    "ו" to arrayOf("װ"),
                    //
                    // Numbers
                    //
                    "1" to arrayOf("!", "1", "¹", "½", "⅓", "¼", "⅕", "⅙", "⅐", "⅛", "⅑", "⅒"),
                    "2" to arrayOf("@", "2", "²", "⅖", "⅔"),
                    "3" to arrayOf("#", "3", "³", "⅗", "¾", "⅜"),
                    "4" to arrayOf("$", "4", "⁴", "⅘", "⅝", "⅚"),
                    "5" to arrayOf("%", "5", "⁵", "⅝", "⅚"),
                    "6" to arrayOf("^", "6", "⁶"),
                    "7" to arrayOf("&", "7", "⁷", "⅞"),
                    "8" to arrayOf("*", "8", "⁸"),
                    "9" to arrayOf("(", "9", "⁹"),
                    "0" to arrayOf(")", "0", "∅", "ⁿ", "⁰"),

                    //
                    // Punctuation
                    //
                    "," to arrayOf("/", "?", "!", ":", ";", "_", "%", "$", "^", "&"),
                    "'" to arrayOf("\""),
                    "." to arrayOf("?", "¿", "‽", ",", "!", ":", ";", "_", "%", "$", "^", "&"),
                    "-" to arrayOf("—", "–", "·"),
                    "?" to arrayOf(),
                    "'" to arrayOf("\"", "‘", "’", "‚", "›", "‹"),
                    "!" to arrayOf("¡"),
                    "\"" to arrayOf("“", "”", "„", "»", "«"),
                    "/" to arrayOf("?", "÷"),
                    "#" to arrayOf("№"),
                    "%" to arrayOf("‰", "℅"),
                    "^" to arrayOf("↑", "↓", "←", "→"),
                    "+" to arrayOf("±"),
                    "<" to arrayOf("≤", "«", "‹", "⟨"),
                    "=" to arrayOf("∞", "≠", "≈"),
                    ">" to arrayOf("≥", "»", "›", "⟩"),
                    "°" to arrayOf("′", "″", "‴"),
                    ";" to arrayOf(":"),
                    //
                    // Currency
                    //
                    "$" to arrayOf("¢", "€", "£", "¥", "₹", "₽", "₺", "₩", "₱", "₿"),
                )
            }
            LayoutSelect.KeyLayout46 -> {
                hashMapOf(
                    //
                    // Latin
                    //
                    "q" to arrayOf("`", "q", "Q"),
                    "w" to arrayOf("~", "w", "W"),
                    "e" to arrayOf(
                        "+",
                        "e",
                        "E",
                        "ê",
                        "ë",
                        "ē",
                        "é",
                        "ě",
                        "è",
                        "ė",
                        "ę",
                        "ȩ",
                        "ḝ",
                        "ə"
                    ),
                    "r" to arrayOf("-", "r", "R"),
                    "t" to arrayOf("=", "t", "T"),
                    "y" to arrayOf("_", "y", "Y", "ÿ", "ұ", "ү", "ӯ", "ў"),
                    "u" to arrayOf("{", "u", "U", "û", "ü", "ū", "ú", "ǔ", "ù"),
                    "i" to arrayOf("}", "i", "I", "î", "ï", "ī", "í", "ǐ", "ì", "į", "ı"),
                    "o" to arrayOf("[", "o", "O", "ô", "ö", "ō", "ó", "ǒ", "ò", "œ", "ø", "õ"),
                    "p" to arrayOf("]", "p", "P"),
                    "a" to arrayOf("\\", "a", "A", "â", "ä", "ā", "á", "ǎ", "à", "æ", "ã", "å"),
                    "s" to arrayOf("|", "s", "S", "ß", "ś", "š", "ş"),
                    "d" to arrayOf("×", "d", "D", "ð"),
                    "f" to arrayOf("÷", "f", "F"),
                    "g" to arrayOf("←", "g", "G", "ğ"),
                    "h" to arrayOf("→", "h", "H"),
                    "j" to arrayOf(formContext[7].component1(), "j", "J"),
                    "k" to arrayOf(formContext[8].component1(), "k", "K"),
                    "l" to arrayOf(formContext[9].component1(), "/", "l", "L", "ł"),
                    ";" to arrayOf(":"),
                    "z" to arrayOf(formContext[0].component1(), "Z", "z", "ž", "ź", "ż"),
                    "x" to arrayOf(formContext[1].component1(), "X", "x", "×"),
                    "c" to arrayOf(formContext[2].component1(), "C", "c", "ç", "ć", "č"),
                    "v" to arrayOf(
                        formContext[3].component1(),
                        "V",
                        "v",
                        "¿",
                        "ü",
                        "ǖ",
                        "ǘ",
                        "ǚ",
                        "ǜ"
                    ),
                    "b" to arrayOf(formContext[4].component1(), "B", "b", "¡"),
                    "n" to arrayOf(formContext[5].component1(), "N", "n", "ñ", "ń"),
                    "m" to arrayOf(formContext[6].component1(), "M", "m"),
                    //
                    // Upper case Latin
                    //
                    "Q" to arrayOf("`", "Q", "q"),
                    "W" to arrayOf("~", "W", "w"),
                    "E" to arrayOf("+", "e", "E", "Ê", "Ë", "Ē", "É", "È", "Ė", "Ę", "Ȩ", "Ḝ", "Ə"),
                    "R" to arrayOf("-", "r", "R"),
                    "T" to arrayOf("=", "t", "T"),
                    "Y" to arrayOf("_", "y", "Y", "Ÿ", "Ұ", "Ү", "Ӯ", "Ў"),
                    "U" to arrayOf("{", "u", "≤", "U", "Û", "Ü", "Ù", "Ú", "Ū"),
                    "I" to arrayOf("}", "i", "≥", "I", "Î", "Ï", "Í", "Ī", "Į", "Ì"),
                    "O" to arrayOf("[", "o", "O", "Ô", "Ö", "Ò", "Ó", "Œ", "Ø", "Ō", "Õ"),
                    "P" to arrayOf("]", "p", "P"),
                    "A" to arrayOf("\\", "a", "A", "Â", "Ä", "Ā", "Á", "À", "Æ", "Ã", "Å"),
                    "S" to arrayOf("|", "s", "S", "ẞ", "Ś", "Š", "Ş"),
                    "D" to arrayOf("×", "d", "D", "Ð"),
                    "F" to arrayOf("÷", "f", "F"),
                    "G" to arrayOf("←", "g", "G", "Ğ"),
                    "H" to arrayOf("→", "h", "H"),
                    "J" to arrayOf("↑", "j", "J"),
                    "K" to arrayOf("↓️️", "k", "K"),
                    "L" to arrayOf("/", "l", "L", "ł"),
                    ";" to arrayOf(":"),
                    "Z" to arrayOf("z", "Z", "`", "Ž", "Ź", "Ż"),
                    "X" to arrayOf("x", "X"),
                    "C" to arrayOf("c", "C", "Ç", "Ć", "Č"),
                    "V" to arrayOf("v", "V"),
                    "B" to arrayOf("b", "B", "¡"),
                    "N" to arrayOf("n", "N", "Ñ", "Ń"),
                    "M" to arrayOf("m", "M"),
                    //
                    // Upper case Cyrillic
                    //
                    "г" to arrayOf("ғ"),
                    "е" to arrayOf("ё"),      // this in fact NOT the same E as before
                    "и" to arrayOf("ӣ", "і"), // і is not i
                    "й" to arrayOf("ј"),      // ј is not j
                    "к" to arrayOf("қ", "ҝ"),
                    "н" to arrayOf("ң", "һ"), // һ is not h
                    "о" to arrayOf("ә", "ө"),
                    "ч" to arrayOf("ҷ", "ҹ"),
                    "ь" to arrayOf("ъ"),
                    //
                    // Cyrillic
                    //
                    "Г" to arrayOf("Ғ"),
                    "Е" to arrayOf("Ё"),      // This In Fact Not The Same E As Before
                    "И" to arrayOf("Ӣ", "І"), // І is sot I
                    "Й" to arrayOf("Ј"),      // Ј is sot J
                    "К" to arrayOf("Қ", "Ҝ"),
                    "Н" to arrayOf("Ң", "Һ"), // Һ is not H
                    "О" to arrayOf("Ә", "Ө"),
                    "Ч" to arrayOf("Ҷ", "Ҹ"),
                    "Ь" to arrayOf("Ъ"),
                    //
                    // Arabic
                    //
                    // This renders weirdly in text editors, but is valid code.
                    "ا" to arrayOf("أ", "إ", "آ", "ء"),
                    "ب" to arrayOf("پ"),
                    "ج" to arrayOf("چ"),
                    "ز" to arrayOf("ژ"),
                    "ف" to arrayOf("ڤ"),
                    "ك" to arrayOf("گ"),
                    "ل" to arrayOf("لا"),
                    "ه" to arrayOf("ه"),
                    "و" to arrayOf("ؤ"),
                    //
                    // Hebrew
                    //
                    // Likewise, this will render oddly, but is still valid code.
                    "ג" to arrayOf("ג׳"),
                    "ז" to arrayOf("ז׳"),
                    "ח" to arrayOf("ח׳"),
                    "צ׳" to arrayOf("צ׳"),
                    "ת" to arrayOf("ת׳"),
                    "י" to arrayOf("ײַ"),
                    "י" to arrayOf("ײ"),
                    "ח" to arrayOf("ױ"),
                    "ו" to arrayOf("װ"),
                    //
                    // Numbers
                    //
                    "1" to arrayOf("!", "1", "¹", "½", "⅓", "¼", "⅕", "⅙", "⅐", "⅛", "⅑", "⅒"),
                    "2" to arrayOf("@", "2", "²", "⅖", "⅔"),
                    "3" to arrayOf("#", "3", "³", "⅗", "¾", "⅜"),
                    "4" to arrayOf("$", "4", "⁴", "⅘", "⅝", "⅚"),
                    "5" to arrayOf("%", "5", "⁵", "⅝", "⅚"),
                    "6" to arrayOf("^", "6", "⁶"),
                    "7" to arrayOf("&", "7", "⁷", "⅞"),
                    "8" to arrayOf("*", "8", "⁸"),
                    "9" to arrayOf("(", "9", "⁹"),
                    "0" to arrayOf(")", "0", "∅", "ⁿ", "⁰"),

                    //
                    // Punctuation
                    //
                    "," to arrayOf("<", "≤", "?", "!", ":", ";", "_", "%", "$", "^", "&"),
                    "'" to arrayOf("\""),
                    "." to arrayOf(">", "≥", ",", "!", ":", ";", "_", "%", "$", "^", "&"),
                    "-" to arrayOf("—", "–", "·"),
                    "?" to arrayOf("¿", "‽"),
                    "'" to arrayOf("\"", "‘", "’", "‚", "›", "‹"),
                    "!" to arrayOf("¡"),
                    "\"" to arrayOf("“", "”", "„", "»", "«"),
                    "/" to arrayOf("?", "÷"),
                    "#" to arrayOf("№"),
                    "%" to arrayOf("‰", "℅"),
                    "^" to arrayOf("↑", "↓", "←", "→"),
                    "+" to arrayOf("±"),
                    "<" to arrayOf("≤", "«", "‹", "⟨"),
                    "=" to arrayOf("∞", "≠", "≈"),
                    ">" to arrayOf("≥", "»", "›", "⟩"),
                    "°" to arrayOf("′", "″", "‴"),
                    //
                    // Currency
                    //
                    "$" to arrayOf("¢", "€", "£", "¥", "₹", "₽", "₺", "₩", "₱", "₿"),
                )
            }
            LayoutSelect.KeyLayout43 -> {
                hashMapOf(
                    //
                    // Latin
                    //
                    "q" to arrayOf("`", "q", "Q"),
                    "w" to arrayOf("~", "w", "W"),
                    "e" to arrayOf(
                        "+",
                        "e",
                        "E",
                        "ê",
                        "ë",
                        "ē",
                        "é",
                        "ě",
                        "è",
                        "ė",
                        "ę",
                        "ȩ",
                        "ḝ",
                        "ə"
                    ),
                    "r" to arrayOf("-", "r", "R"),
                    "t" to arrayOf("=", "t", "T"),
                    "y" to arrayOf("_", "y", "Y", "ÿ", "ұ", "ү", "ӯ", "ў"),
                    "u" to arrayOf("{", "u", "U", "û", "ü", "ū", "ú", "ǔ", "ù"),
                    "i" to arrayOf("}", "i", "I", "î", "ï", "ī", "í", "ǐ", "ì", "į", "ı"),
                    "o" to arrayOf("[", "o", "O", "ô", "ö", "ō", "ó", "ǒ", "ò", "œ", "ø", "õ"),
                    "p" to arrayOf("]", "p", "P"),
                    "a" to arrayOf("\\", "a", "A", "â", "ä", "ā", "á", "ǎ", "à", "æ", "ã", "å"),
                    "s" to arrayOf("|", "s", "S", "ß", "ś", "š", "ş"),
                    "d" to arrayOf("×", "d", "D", "ð"),
                    "f" to arrayOf("÷", "f", "F"),
                    "g" to arrayOf("←", "g", "G", "ğ"),
                    "h" to arrayOf("→", "h", "H"),
                    "j" to arrayOf("<", "j", "J"),
                    "k" to arrayOf(">", "k", "K"),
                    "l" to arrayOf(formContext[9].component1(), "/", "l", "L", "ł"),
                    "z" to arrayOf(formContext[0].component1(), "Z", "z", "ž", "ź", "ż"),
                    "x" to arrayOf(formContext[1].component1(), "X", "x", "×"),
                    "c" to arrayOf(formContext[2].component1(), "C", "c", "ç", "ć", "č"),
                    "v" to arrayOf(
                        formContext[3].component1(),
                        "V",
                        "v",
                        "¿",
                        "ü",
                        "ǖ",
                        "ǘ",
                        "ǚ",
                        "ǜ"
                    ),
                    "b" to arrayOf(formContext[4].component1(), "B", "b", "¡"),
                    "n" to arrayOf(formContext[5].component1(), "N", "n", "ñ", "ń"),
                    "m" to arrayOf(formContext[6].component1(), "M", "m"),
                    //
                    // Upper case Latin
                    //
                    "Q" to arrayOf("`", "Q", "q"),
                    "W" to arrayOf("~", "W", "w"),
                    "E" to arrayOf("+", "e", "E", "Ê", "Ë", "Ē", "É", "È", "Ė", "Ę", "Ȩ", "Ḝ", "Ə"),
                    "R" to arrayOf("-", "r", "R"),
                    "T" to arrayOf("=", "t", "T"),
                    "Y" to arrayOf("_", "y", "Y", "Ÿ", "Ұ", "Ү", "Ӯ", "Ў"),
                    "U" to arrayOf("{", "u", "≤", "U", "Û", "Ü", "Ù", "Ú", "Ū"),
                    "I" to arrayOf("}", "i", "≥", "I", "Î", "Ï", "Í", "Ī", "Į", "Ì"),
                    "O" to arrayOf("[", "o", "O", "Ô", "Ö", "Ò", "Ó", "Œ", "Ø", "Ō", "Õ"),
                    "P" to arrayOf("]", "p", "P"),
                    "A" to arrayOf("\\", "a", "A", "Â", "Ä", "Ā", "Á", "À", "Æ", "Ã", "Å"),
                    "S" to arrayOf("|", "s", "S", "ẞ", "Ś", "Š", "Ş"),
                    "D" to arrayOf("×", "d", "D", "Ð"),
                    "F" to arrayOf("÷", "f", "F"),
                    "G" to arrayOf("←", "g", "G", "Ğ"),
                    "H" to arrayOf("→", "h", "H"),
                    "J" to arrayOf("↑", "j", "J"),
                    "K" to arrayOf("↓️️", "k", "K"),
                    "L" to arrayOf("/", "l", "L", "ł"),
                    "Z" to arrayOf("z", "Z", "`", "Ž", "Ź", "Ż"),
                    "X" to arrayOf("x", "X"),
                    "C" to arrayOf("c", "C", "Ç", "Ć", "Č"),
                    "V" to arrayOf("v", "V"),
                    "B" to arrayOf("b", "B", "¡"),
                    "N" to arrayOf("n", "N", "Ñ", "Ń"),
                    "M" to arrayOf("m", "M"),
                    //
                    // Upper case Cyrillic
                    //
                    "г" to arrayOf("ғ"),
                    "е" to arrayOf("ё"),      // this in fact NOT the same E as before
                    "и" to arrayOf("ӣ", "і"), // і is not i
                    "й" to arrayOf("ј"),      // ј is not j
                    "к" to arrayOf("қ", "ҝ"),
                    "н" to arrayOf("ң", "һ"), // һ is not h
                    "о" to arrayOf("ә", "ө"),
                    "ч" to arrayOf("ҷ", "ҹ"),
                    "ь" to arrayOf("ъ"),
                    //
                    // Cyrillic
                    //
                    "Г" to arrayOf("Ғ"),
                    "Е" to arrayOf("Ё"),      // This In Fact Not The Same E As Before
                    "И" to arrayOf("Ӣ", "І"), // І is sot I
                    "Й" to arrayOf("Ј"),      // Ј is sot J
                    "К" to arrayOf("Қ", "Ҝ"),
                    "Н" to arrayOf("Ң", "Һ"), // Һ is not H
                    "О" to arrayOf("Ә", "Ө"),
                    "Ч" to arrayOf("Ҷ", "Ҹ"),
                    "Ь" to arrayOf("Ъ"),
                    //
                    // Arabic
                    //
                    // This renders weirdly in text editors, but is valid code.
                    "ا" to arrayOf("أ", "إ", "آ", "ء"),
                    "ب" to arrayOf("پ"),
                    "ج" to arrayOf("چ"),
                    "ز" to arrayOf("ژ"),
                    "ف" to arrayOf("ڤ"),
                    "ك" to arrayOf("گ"),
                    "ل" to arrayOf("لا"),
                    "ه" to arrayOf("ه"),
                    "و" to arrayOf("ؤ"),
                    //
                    // Hebrew
                    //
                    // Likewise, this will render oddly, but is still valid code.
                    "ג" to arrayOf("ג׳"),
                    "ז" to arrayOf("ז׳"),
                    "ח" to arrayOf("ח׳"),
                    "צ׳" to arrayOf("צ׳"),
                    "ת" to arrayOf("ת׳"),
                    "י" to arrayOf("ײַ"),
                    "י" to arrayOf("ײ"),
                    "ח" to arrayOf("ױ"),
                    "ו" to arrayOf("װ"),
                    //
                    // Numbers
                    //
                    "1" to arrayOf("!", "1", "¹", "½", "⅓", "¼", "⅕", "⅙", "⅐", "⅛", "⅑", "⅒"),
                    "2" to arrayOf("@", "2", "²", "⅖", "⅔"),
                    "3" to arrayOf("#", "3", "³", "⅗", "¾", "⅜"),
                    "4" to arrayOf("$", "4", "⁴", "⅘", "⅝", "⅚"),
                    "5" to arrayOf("%", "5", "⁵", "⅝", "⅚"),
                    "6" to arrayOf("^", "6", "⁶"),
                    "7" to arrayOf("&", "7", "⁷", "⅞"),
                    "8" to arrayOf("*", "8", "⁸"),
                    "9" to arrayOf("(", "9", "⁹"),
                    "0" to arrayOf(")", "0", "∅", "ⁿ", "⁰"),

                    //
                    // Punctuation
                    //
                    "," to arrayOf(",", "“", "/", "!", "_", "%", "$", "^", "&"),
                    "'" to arrayOf("\""),
                    "." to arrayOf(
                        ".",
                        "?",
                        "”",
                        "¿",
                        "‽",
                        ",",
                        "!",
                        ":",
                        ";",
                        "_",
                        "%",
                        "$",
                        "^",
                        "&"
                    ),
                    "-" to arrayOf("—", "–", "·"),
                    "?" to arrayOf(),
                    "'" to arrayOf("\"", "‘", "’", "‚", "›", "‹"),
                    "!" to arrayOf("¡"),
                    "\"" to arrayOf("“", "”", "„", "»", "«"),
                    "/" to arrayOf("?", "÷"),
                    "#" to arrayOf("№"),
                    "%" to arrayOf("‰", "℅"),
                    "^" to arrayOf("↑", "↓", "←", "→"),
                    "+" to arrayOf("±"),
                    "<" to arrayOf("≤", "«", "‹", "⟨"),
                    "=" to arrayOf("∞", "≠", "≈"),
                    ">" to arrayOf("≥", "»", "›", "⟩"),
                    "°" to arrayOf("′", "″", "‴"),
                    ";" to arrayOf(":"),
                    //
                    // Currency
                    //
                    "$" to arrayOf("¢", "€", "£", "¥", "₹", "₽", "₺", "₩", "₱", "₿"),
                )
            }
            LayoutSelect.KeyLayout45NumberBelow -> {
                hashMapOf(
                    //
                    // Latin
                    //
                    "q" to arrayOf("`", "q", "Q"),
                    "w" to arrayOf("~", "w", "W"),
                    "e" to arrayOf(
                        "+",
                        "e",
                        "E",
                        "ê",
                        "ë",
                        "ē",
                        "é",
                        "ě",
                        "è",
                        "ė",
                        "ę",
                        "ȩ",
                        "ḝ",
                        "ə"
                    ),
                    "r" to arrayOf("-", "r", "R"),
                    "t" to arrayOf("=", "t", "T"),
                    "y" to arrayOf("_", "y", "Y", "ÿ", "ұ", "ү", "ӯ", "ў"),
                    "u" to arrayOf("{", "u", "U", "û", "ü", "ū", "ú", "ǔ", "ù"),
                    "i" to arrayOf("}", "i", "I", "î", "ï", "ī", "í", "ǐ", "ì", "į", "ı"),
                    "o" to arrayOf("[", "o", "O", "ô", "ö", "ō", "ó", "ǒ", "ò", "œ", "ø", "õ"),
                    "p" to arrayOf("]", "p", "P"),
                    "a" to arrayOf("\\", "a", "A", "â", "ä", "ā", "á", "ǎ", "à", "æ", "ã", "å"),
                    "s" to arrayOf("|", "s", "S", "ß", "ś", "š", "ş"),
                    "d" to arrayOf("×", "d", "D", "ð"),
                    "f" to arrayOf("÷", "f", "F"),
                    "g" to arrayOf("←", "g", "G", "ğ"),
                    "h" to arrayOf("→", "h", "H"),
                    "j" to arrayOf("<", "j", "J"),
                    "k" to arrayOf(">", "k", "K"),
                    "l" to arrayOf(formContext[9].component1(), "/", "l", "L", "ł"),
                    "z" to arrayOf(formContext[0].component1(), "Z", "z", "ž", "ź", "ż"),
                    "x" to arrayOf(formContext[1].component1(), "X", "x", "×"),
                    "c" to arrayOf(formContext[2].component1(), "C", "c", "ç", "ć", "č"),
                    "v" to arrayOf(
                        formContext[3].component1(),
                        "V",
                        "v",
                        "¿",
                        "ü",
                        "ǖ",
                        "ǘ",
                        "ǚ",
                        "ǜ"
                    ),
                    "b" to arrayOf(formContext[4].component1(), "B", "b", "¡"),
                    "n" to arrayOf(formContext[5].component1(), "N", "n", "ñ", "ń"),
                    "m" to arrayOf(formContext[6].component1(), "M", "m"),
                    //
                    // Upper case Latin
                    //
                    "Q" to arrayOf("`", "Q", "q"),
                    "W" to arrayOf("~", "W", "w"),
                    "E" to arrayOf("+", "e", "E", "Ê", "Ë", "Ē", "É", "È", "Ė", "Ę", "Ȩ", "Ḝ", "Ə"),
                    "R" to arrayOf("-", "r", "R"),
                    "T" to arrayOf("=", "t", "T"),
                    "Y" to arrayOf("_", "y", "Y", "Ÿ", "Ұ", "Ү", "Ӯ", "Ў"),
                    "U" to arrayOf("{", "u", "≤", "U", "Û", "Ü", "Ù", "Ú", "Ū"),
                    "I" to arrayOf("}", "i", "≥", "I", "Î", "Ï", "Í", "Ī", "Į", "Ì"),
                    "O" to arrayOf("[", "o", "O", "Ô", "Ö", "Ò", "Ó", "Œ", "Ø", "Ō", "Õ"),
                    "P" to arrayOf("]", "p", "P"),
                    "A" to arrayOf("\\", "a", "A", "Â", "Ä", "Ā", "Á", "À", "Æ", "Ã", "Å"),
                    "S" to arrayOf("|", "s", "S", "ẞ", "Ś", "Š", "Ş"),
                    "D" to arrayOf("×", "d", "D", "Ð"),
                    "F" to arrayOf("÷", "f", "F"),
                    "G" to arrayOf("←", "g", "G", "Ğ"),
                    "H" to arrayOf("→", "h", "H"),
                    "J" to arrayOf("↑", "j", "J"),
                    "K" to arrayOf("↓️️", "k", "K"),
                    "L" to arrayOf("/", "l", "L", "ł"),
                    "Z" to arrayOf("z", "Z", "`", "Ž", "Ź", "Ż"),
                    "X" to arrayOf("x", "X"),
                    "C" to arrayOf("c", "C", "Ç", "Ć", "Č"),
                    "V" to arrayOf("v", "V"),
                    "B" to arrayOf("b", "B", "¡"),
                    "N" to arrayOf("n", "N", "Ñ", "Ń"),
                    "M" to arrayOf("m", "M"),
                    //
                    // Upper case Cyrillic
                    //
                    "г" to arrayOf("ғ"),
                    "е" to arrayOf("ё"),      // this in fact NOT the same E as before
                    "и" to arrayOf("ӣ", "і"), // і is not i
                    "й" to arrayOf("ј"),      // ј is not j
                    "к" to arrayOf("қ", "ҝ"),
                    "н" to arrayOf("ң", "һ"), // һ is not h
                    "о" to arrayOf("ә", "ө"),
                    "ч" to arrayOf("ҷ", "ҹ"),
                    "ь" to arrayOf("ъ"),
                    //
                    // Cyrillic
                    //
                    "Г" to arrayOf("Ғ"),
                    "Е" to arrayOf("Ё"),      // This In Fact Not The Same E As Before
                    "И" to arrayOf("Ӣ", "І"), // І is sot I
                    "Й" to arrayOf("Ј"),      // Ј is sot J
                    "К" to arrayOf("Қ", "Ҝ"),
                    "Н" to arrayOf("Ң", "Һ"), // Һ is not H
                    "О" to arrayOf("Ә", "Ө"),
                    "Ч" to arrayOf("Ҷ", "Ҹ"),
                    "Ь" to arrayOf("Ъ"),
                    //
                    // Arabic
                    //
                    // This renders weirdly in text editors, but is valid code.
                    "ا" to arrayOf("أ", "إ", "آ", "ء"),
                    "ب" to arrayOf("پ"),
                    "ج" to arrayOf("چ"),
                    "ز" to arrayOf("ژ"),
                    "ف" to arrayOf("ڤ"),
                    "ك" to arrayOf("گ"),
                    "ل" to arrayOf("لا"),
                    "ه" to arrayOf("ه"),
                    "و" to arrayOf("ؤ"),
                    //
                    // Hebrew
                    //
                    // Likewise, this will render oddly, but is still valid code.
                    "ג" to arrayOf("ג׳"),
                    "ז" to arrayOf("ז׳"),
                    "ח" to arrayOf("ח׳"),
                    "צ׳" to arrayOf("צ׳"),
                    "ת" to arrayOf("ת׳"),
                    "י" to arrayOf("ײַ"),
                    "י" to arrayOf("ײ"),
                    "ח" to arrayOf("ױ"),
                    "ו" to arrayOf("װ"),
                    //
                    // Numbers
                    //
                    "1" to arrayOf("!", "1", "¹", "½", "⅓", "¼", "⅕", "⅙", "⅐", "⅛", "⅑", "⅒"),
                    "2" to arrayOf("@", "2", "²", "⅖", "⅔"),
                    "3" to arrayOf("#", "3", "³", "⅗", "¾", "⅜"),
                    "4" to arrayOf("$", "4", "⁴", "⅘", "⅝", "⅚"),
                    "5" to arrayOf("%", "5", "⁵", "⅝", "⅚"),
                    "6" to arrayOf("^", "6", "⁶"),
                    "7" to arrayOf("&", "7", "⁷", "⅞"),
                    "8" to arrayOf("*", "8", "⁸"),
                    "9" to arrayOf("(", "9", "⁹"),
                    "0" to arrayOf(")", "0", "∅", "ⁿ", "⁰"),

                    //
                    // Punctuation
                    //
                    "," to arrayOf("/", "?", "!", ":", ";", "_", "%", "$", "^", "&"),
                    "'" to arrayOf("\""),
                    "." to arrayOf("?", "¿", "‽", ",", "!", ":", ";", "_", "%", "$", "^", "&"),
                    "-" to arrayOf("—", "–", "·"),
                    "?" to arrayOf(),
                    "'" to arrayOf("\"", "‘", "’", "‚", "›", "‹"),
                    "!" to arrayOf("¡"),
                    "\"" to arrayOf("“", "”", "„", "»", "«"),
                    "/" to arrayOf("?", "÷"),
                    "#" to arrayOf("№"),
                    "%" to arrayOf("‰", "℅"),
                    "^" to arrayOf("↑", "↓", "←", "→"),
                    "+" to arrayOf("±"),
                    "<" to arrayOf("≤", "«", "‹", "⟨"),
                    "=" to arrayOf("∞", "≠", "≈"),
                    ">" to arrayOf("≥", "»", "›", "⟩"),
                    "°" to arrayOf("′", "″", "‴"),
                    ";" to arrayOf(":"),
                    //
                    // Currency
                    //
                    "$" to arrayOf("¢", "€", "£", "¥", "₹", "₽", "₺", "₩", "₱", "₿"),
                )
            }
        }
    }

    fun getCurrentKeyboardName(): String = ThemeManager.prefs.layoutSelect.getValue().keyboardName
}