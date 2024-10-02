/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2023 Fcitx5 for Android Contributors
 */
package org.fcitx.fcitx5.android.input.popup

/**
 * based on
 * [fcitx5/src/im/keyboard/longpress.cpp](https://github.com/fcitx/fcitx5/blob/5.0.18/src/im/keyboard/longpress.cpp#L15)
 */
val PopupPreset: Map<String, Array<String>> = hashMapOf(
    //
    // Latin
    //
    "q" to arrayOf("q", "Q", "`"),
    "w" to arrayOf("w", "W", "~"),
    "e" to arrayOf("e", "+", "E", "ê", "ë", "ē", "é", "ě", "è", "ė", "ę", "ȩ", "ḝ", "ə"),
    "r" to arrayOf("r", "-", "R"),
    "t" to arrayOf("t", "=", "T"),
    "y" to arrayOf("y", "_", "Y", "ÿ", "ұ", "ү", "ӯ", "ў"),
    "u" to arrayOf("u", "<", "≤", "U", "û", "ü", "ū", "ú", "ǔ", "ù"),
    "i" to arrayOf("i", ">", "≥", "I", "î", "ï", "ī", "í", "ǐ", "ì", "į", "ı"),
    "o" to arrayOf("o", "[", "{", "O", "ô", "ö", "ō", "ó", "ǒ", "ò", "œ", "ø", "õ"),
    "p" to arrayOf("p", "]", "}", "P"),
    "a" to arrayOf("a", "\\", "A", "â", "ä", "ā", "á", "ǎ", "à", "æ", "ã", "å"),
    "s" to arrayOf("s", "|", "S", "ß", "ś", "š", "ş"),
    "d" to arrayOf("d", "×", "D", "ð"),
    "f" to arrayOf("f", "÷", "F"),
    "g" to arrayOf("g", "←", "G", "ğ"),
    "h" to arrayOf("h", "→", "H"),
    "j" to arrayOf("j", "↑", "J"),
    "k" to arrayOf("k", "↓️️", "K"),
    "l" to arrayOf("l", "/", "L", "ł"),
    ";" to arrayOf(":"),
    "z" to arrayOf("z", "Z", "ž", "ź", "ż"),
    "x" to arrayOf("x", "X", "×"),
    "c" to arrayOf("c", "C", "ç", "ć", "č"),
    "v" to arrayOf("v", "V", "¿", "ü", "ǖ", "ǘ", "ǚ", "ǜ"),
    "b" to arrayOf("b", "B", "¡"),
    "n" to arrayOf("n", "N", "ñ", "ń"),
    "m" to arrayOf("m", "M"),
    "," to arrayOf("."),
    //
    // Upper case Latin
    //
    "Q" to arrayOf("q", "Q", "`"),
    "W" to arrayOf("w", "W", "~"),
    "E" to arrayOf("e", "+", "E", "Ê", "Ë", "Ē", "É", "È", "Ė", "Ę", "Ȩ", "Ḝ", "Ə"),
    "R" to arrayOf("r", "-", "R"),
    "T" to arrayOf("t", "=", "T"),
    "Y" to arrayOf("y", "_", "Y", "Ÿ", "Ұ", "Ү", "Ӯ", "Ў"),
    "U" to arrayOf("u", "<", "≤", "U", "Û", "Ü", "Ù", "Ú", "Ū"),
    "I" to arrayOf("i", ">", "≥", "I", "Î", "Ï", "Í", "Ī", "Į", "Ì"),
    "O" to arrayOf("o", "[", "{", "O", "Ô", "Ö", "Ò", "Ó", "Œ", "Ø", "Ō", "Õ"),
    "P" to arrayOf("p", "]", "}", "P"),
    "A" to arrayOf("a", "\\", "A", "Â", "Ä", "Ā", "Á", "À", "Æ", "Ã", "Å"),
    "S" to arrayOf("s", "|", "S", "ẞ", "Ś", "Š", "Ş"),
    "D" to arrayOf("d", "×", "D", "Ð"),
    "F" to arrayOf("f", "÷", "F"),
    "G" to arrayOf("g", "←", "G", "Ğ"),
    "H" to arrayOf("h", "→", "H"),
    "J" to arrayOf("j", "↑", "J"),
    "K" to arrayOf("k", "↓️️", "K"),
    "L" to arrayOf("l", "/", "L", "ł"),
    ";" to arrayOf(":"),
    "Z" to arrayOf("z", "Z", "`", "Ž", "Ź", "Ż"),
    "X" to arrayOf("x", "X"),
    "C" to arrayOf("c", "C", "Ç", "Ć", "Č"),
    "V" to arrayOf("v", "V"),
    "B" to arrayOf("b", "B", "¡"),
    "N" to arrayOf("n", "N", "Ñ", "Ń"),
    "M" to arrayOf("m", "M"),
    "," to arrayOf("."),
    "'" to arrayOf("\""),
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
    "1" to arrayOf("1", "!", "¹", "½", "⅓", "¼", "⅕", "⅙", "⅐", "⅛", "⅑", "⅒"),
    "2" to arrayOf("2", "@","²", "⅖", "⅔"),
    "3" to arrayOf("3", "#","³", "⅗", "¾", "⅜"),
    "4" to arrayOf("4", "$","⁴", "⅘", "⅝", "⅚"),
    "5" to arrayOf("5", "%","⁵", "⅝", "⅚"),
    "6" to arrayOf("6", "^","⁶"),
    "7" to arrayOf("7", "&","⁷", "⅞"),
    "8" to arrayOf("8", "*","⁸"),
    "9" to arrayOf("9", "(","⁹"),
    "0" to arrayOf("0", ")","∅", "ⁿ", "⁰"),

    //
    // Punctuation
    //
    "." to arrayOf(",", "?", "!", ":", ";", "_", "%", "$", "^", "&"),
    "-" to arrayOf("—", "–", "·"),
    "?" to arrayOf("¿", "‽"),
    "'" to arrayOf("\"", "‘", "’", "‚", "›", "‹"),
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
