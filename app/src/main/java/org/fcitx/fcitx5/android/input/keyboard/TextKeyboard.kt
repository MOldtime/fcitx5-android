/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2023 Fcitx5 for Android Contributors
 */
package org.fcitx.fcitx5.android.input.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import androidx.annotation.Keep
import androidx.core.view.allViews
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxEvent
import org.fcitx.fcitx5.android.core.InputMethodEntry
import org.fcitx.fcitx5.android.core.KeyState
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.ScancodeMapping
import org.fcitx.fcitx5.android.data.prefs.AppPrefs
import org.fcitx.fcitx5.android.data.prefs.ManagedPreference
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.FcitxInputMethodService
import org.fcitx.fcitx5.android.input.clipboard.ClipboardWindow
import org.fcitx.fcitx5.android.input.dependency.inputMethodService
import org.fcitx.fcitx5.android.input.keyboard.CustomGestureView.GestureType
import org.fcitx.fcitx5.android.input.keyboard.CustomGestureView.OnGestureListener
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Behavior
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Popup
import org.fcitx.fcitx5.android.input.popup.PopupAction
import splitties.views.dsl.core.textView
import splitties.views.imageResource
import splitties.views.onClick
import kotlin.math.log

@SuppressLint("ViewConstructor")
class TextKeyboard(
    context: Context,
    theme: Theme
) : BaseKeyboard(context, theme, Layout) {

    enum class CapsState { None, Once, Lock }

    companion object {
        const val Name = "Text"

        val Layout: List<List<KeyDef>> = listOf(
            /** 33键
            listOf(
                AlphabetKeyNew("Q"),
                AlphabetKeyNew("W"),
                AlphabetKeyNew("R"),
                AlphabetKeyNew("T"),
                AlphabetKeyNew("Y"),
                AlphabetKeyNew("L"),
                AlphabetKeyNew("P"),
            ),
            listOf(
                AlphabetKeyNew("S"),
                AlphabetKeyNew("D"),
                AlphabetKeyNew("F"),
                AlphabetKeyNew("G"),
                AlphabetKeyNew("H"),
                AlphabetKeyNew("J"),
                AlphabetKeyNew("K"),
            ),
            listOf(
                AlphabetKeyNew("Z"),
                AlphabetKeyNew("X", "1"),
                AlphabetKeyNew("C", "2"),
                AlphabetKeyNew("V", "3"),
                AlphabetKeyNew("B", "4"),
                AlphabetKeyNew("N", "5"),
                AlphabetKeyNew("M"),
            ),
            listOf(
                CapsKey(),
                AlphabetKeyNew("A", "6"),
                AlphabetKeyNew("E", "7"),
                AlphabetKeyNew("U", "8"),
                AlphabetKeyNew("I", "9"),
                AlphabetKeyNew("O", "0"),
                BackspaceKey(),
            ),
            listOf(
                LayoutSwitchNumber("?123", ""),
                LanguageKey(),
                SymbolKeyId(",", R.id.button_left, variant = KeyDef.Appearance.Variant.Alternative),
                SpaceKey(),
                SymbolKeyId(".", R.id.button_right , variant = KeyDef.Appearance.Variant.Alternative),
                ReturnKey()
            )
            **/
            /** 26键
            listOf(
                AlphabetKey("Q", "1"),
                AlphabetKey("W", "2"),
                AlphabetKey("E", "3"),
                AlphabetKey("R", "4"),
                AlphabetKey("T", "5"),
                AlphabetKey("Y", "6"),
                AlphabetKey("U", "7"),
                AlphabetKey("I", "8"),
                AlphabetKey("O", "9"),
                AlphabetKey("P", "0")
            ),
            listOf(
                AlphabetKey("A", "@"),
                AlphabetKey("S", "*"),
                AlphabetKey("D", "+"),
                AlphabetKey("F", "-"),
                AlphabetKey("G", "="),
                AlphabetKey("H", "/"),
                AlphabetKey("J", "#"),
                AlphabetKey("K", "("),
                AlphabetKey("L", ")"),
            ),
            listOf(
                CapsKey(),
                AlphabetKey("Z", "'"),
                AlphabetKey("X", ":"),
                AlphabetKey("C", "\""),
                AlphabetKey("V", "?"),
                AlphabetKey("B", "!"),
                AlphabetKey("N", "~"),
                AlphabetKey("M", "\\"),
                BackspaceKey()
            ),
            listOf(
                LayoutSwitchNumber("?123", ""),
                LanguageKey(),
                SpaceKey(),
                SymbolKeyId(",", R.id.button_left, variant = KeyDef.Appearance.Variant.Alternative),
                SymbolKeyId(".", R.id.button_right , variant = KeyDef.Appearance.Variant.Alternative),
                ReturnKey()
            )
            **/
//            /** 46键
            listOf(
                AlphabetKey("1", "!"),
                AlphabetKey("2", "@"),
                AlphabetKey("3", "#"),
                AlphabetKey("4", "$"),
                AlphabetKey("5", "%"),
                AlphabetKey("6", "^"),
                AlphabetKey("7", "&"),
                AlphabetKey("8", "*"),
                AlphabetKey("9", "("),
                AlphabetKey("0", ")")
            ),
            listOf(
                AlphabetKey("Q", "`"),
                AlphabetKey("W", "~"),
                AlphabetKey("E", "+"),
                AlphabetKey("R", "-"),
                AlphabetKey("T", "="),
                AlphabetKey("Y", "_"),
                AlphabetKey("U", "<"),
                AlphabetKey("I", ">"),
                AlphabetKey("O", "["),
                AlphabetKey("P", "]")
            ),
            listOf(
                AlphabetKeyNew("A", "\\", percentWidth = 0.095f),
                AlphabetKeyNew("S", "|", percentWidth = 0.095f),
                AlphabetKeyNew("D", "×", percentWidth = 0.095f),
                AlphabetKeyNew("F", "÷", percentWidth = 0.095f),
                AlphabetKeyNew("G", "←", percentWidth = 0.095f),
                AlphabetKeyNew("H", "→", percentWidth = 0.095f),
                AlphabetKeyNew("J", "↑", percentWidth = 0.095f),
                AlphabetKeyNew("K", "↓", percentWidth = 0.095f),
                AlphabetKeyNew("L", "/", percentWidth = 0.095f),
                AlphabetKeyNew(";", ":", percentWidth = 0.095f),
            ),
            listOf(
                CapsKey(),
                KeyDef(
                    Appearance.AltText(
                        displayText = "Z",
                        altText = "全选",
                        textSize = 23f,
                        variant = Appearance.Variant.Normal
                    ),
                    setOf(
                        Behavior.Swipe(KeyAction.PerformContextMenuAction(android.R.id.selectAll)),
                        Behavior.Press(KeyAction.FcitxKeyAction("Z")),
                    ),
                    arrayOf(
                        Popup.Keyboard("Z")
                    )
                ),
                KeyDef(
                    Appearance.AltText(
                        displayText = "X",
                        altText = "剪切",
                        textSize = 23f,
                        variant = Appearance.Variant.Normal
                    ),
                    setOf(
                        Behavior.Swipe(KeyAction.PerformContextMenuAction(android.R.id.cut)),
                        Behavior.Press(KeyAction.FcitxKeyAction("X")),
                    ),
                    arrayOf(
                        Popup.Keyboard("X")
                    )
                ),
                KeyDef(
                    Appearance.AltText(
                        displayText = "C",
                        altText = "复制",
                        textSize = 23f,
                        variant = Appearance.Variant.Normal
                    ),
                    setOf(
                        Behavior.Swipe(KeyAction.PerformContextMenuAction(android.R.id.copy)),
                        Behavior.Press(KeyAction.FcitxKeyAction("C")),
                    ),
                    arrayOf(
                        Popup.Keyboard("C")
                    )
                ),
                KeyDef(
                    Appearance.AltText(
                        displayText = "V",
                        altText = "粘贴",
                        textSize = 23f,
                        variant = Appearance.Variant.Normal
                    ),
                    setOf(
                        Behavior.Swipe(KeyAction.PerformContextMenuAction(android.R.id.paste)),
                        Behavior.Press(KeyAction.FcitxKeyAction("V")),
                    ),
                    arrayOf(
                        Popup.Keyboard("V")
                    )
                ),
                KeyDef(
                    Appearance.AltText(
                        displayText = "B",
                        altText = " ",
                        textSize = 23f,
                        variant = Appearance.Variant.Normal
                    ),
                    setOf(
                        Behavior.Swipe(KeyAction.attachWindow(ClipboardWindow())),
                        Behavior.Press(KeyAction.FcitxKeyAction("B")),
                    ),
                    arrayOf(
                        Popup.Keyboard("B")
                    )
                ),
                AlphabetTextKey("N"),
                AlphabetTextKey("M"),

                BackspaceKey()
            ),
            listOf(
                LayoutSwitchNumber("?123", ""),
                Emoji(),
                LanguageKey(),
                SpaceKey(),
                SymbolKeyAlt(",","."),
                SymbolKeyAlt("'","\""),
                ReturnKey()
            )
//            **/
        )
    }


    val symbolLeft: TextKeyView by lazy { findViewById(R.id.button_left) }
    val symbolRight: TextKeyView by lazy { findViewById(R.id.button_right) }

    val caps: ImageKeyView by lazy { findViewById(R.id.button_caps) }
    val backspace: ImageKeyView by lazy { findViewById(R.id.button_backspace) }
    val quickphrase: ImageKeyView by lazy { findViewById(R.id.button_quickphrase) }
    val lang: ImageKeyView by lazy { findViewById(R.id.button_lang) }
    val space: TextKeyView by lazy { findViewById(R.id.button_space) }
    val `return`: ImageKeyView by lazy { findViewById(R.id.button_return) }

    private val showLangSwitchKey = AppPrefs.getInstance().keyboard.showLangSwitchKey

    @Keep
    private val showLangSwitchKeyListener = ManagedPreference.OnChangeListener<Boolean> { _, v ->
        updateLangSwitchKey(v)
    }

    private val keepLettersUppercase by AppPrefs.getInstance().keyboard.keepLettersUppercase

    init {
        updateLangSwitchKey(showLangSwitchKey.getValue())
        showLangSwitchKey.registerOnChangeListener(showLangSwitchKeyListener)
    }

    private val textKeys: List<TextKeyView> by lazy {
        allViews.filterIsInstance(TextKeyView::class.java).toList()
    }

    private var capsState: CapsState = CapsState.None

    private fun transformAlphabet(c: String): String {
        return when (capsState) {
            CapsState.None -> c.lowercase()
            else -> c.uppercase()
        }
    }

    private var punctuationMapping: Map<String, String> = mapOf()
    private fun transformPunctuation(p: String) = punctuationMapping.getOrDefault(p, p)

    override fun onAction(action: KeyAction, source: KeyActionListener.Source) {
        var transformed = action
        when (action) {
            is KeyAction.FcitxKeyAction -> when (source) {
                KeyActionListener.Source.Keyboard -> {
                    when (capsState) {
                        CapsState.None -> {
                            transformed = action.copy(act = action.act.lowercase())
                        }
                        CapsState.Once -> {
                            transformed = action.copy(
                                act = action.act.uppercase(),
                                states = KeyStates(KeyState.Virtual, KeyState.Shift)
                            )
                            switchCapsState()
                        }
                        CapsState.Lock -> {
                            transformed = action.copy(
                                act = action.act.uppercase(),
                                states = KeyStates(KeyState.Virtual, KeyState.CapsLock)
                            )
                        }
                    }
                }
                KeyActionListener.Source.Popup -> {
                    if (capsState == CapsState.Once) {
                        switchCapsState()
                    }
                }
            }
            is KeyAction.CapsAction -> switchCapsState(action.lock)
            else -> {}
        }
        super.onAction(transformed, source)
    }

    override fun onAttach() {
        capsState = CapsState.None
        updateCapsButtonIcon()
        updateAlphabetKeys()
    }

    override fun onReturnDrawableUpdate(returnDrawable: Int) {
        `return`.img.imageResource = returnDrawable
    }

    override fun onCandidateUpdate(status: Boolean) {
        if (status) {
            caps.setOnClickListener { _ ->
                super.onAction(KeyAction.FcitxKeyAction("Tab", 15), KeyActionListener.Source.Keyboard)
            }
            caps.setOnLongClickListener { _ ->
                super.onAction(KeyAction.FcitxKeyAction("Tab", 15, KeyStates(KeyState.Virtual, KeyState.Shift)), KeyActionListener.Source.Keyboard)
                true
            }

//            symbolLeft.setOnClickListener { _ ->
//                super.onAction(KeyAction.FcitxKeyAction(";"), KeyActionListener.Source.Keyboard)
//            }
//            symbolRight.setOnClickListener { _ ->
//                super.onAction(KeyAction.FcitxKeyAction("'"), KeyActionListener.Source.Keyboard)
//            }

            space.swipeRewrite(KeyAction.FcitxKeyAction("Space", ScancodeMapping.KEY_SPACE, KeyStates(KeyState.Virtual, KeyState.Shift)))

        } else {
            caps.setOnClickListener { _ ->
                onAction(KeyAction.CapsAction(false), KeyActionListener.Source.Keyboard)
            }
            caps.setOnLongClickListener { _ ->
                true
            }

//            symbolLeft.setOnClickListener { _ ->
//                super.onAction(KeyAction.FcitxKeyAction("，"), KeyActionListener.Source.Keyboard)
//            }
//            symbolRight.setOnClickListener { _ ->
//                super.onAction(KeyAction.FcitxKeyAction("。"), KeyActionListener.Source.Keyboard)
//            }

            space.swipeRewrite(KeyAction.LangSwitchAction)
        }
    }

    override fun onPunctuationUpdate(mapping: Map<String, String>) {
        punctuationMapping = mapping
        updatePunctuationKeys()
    }

    override fun onInputMethodUpdate(ime: InputMethodEntry) {
        space.mainText.text = buildString {
            append(ime.displayName)
            ime.subMode.run { label.ifEmpty { name.ifEmpty { null } } }?.let { append(" ($it)") }
        }
        if (capsState != CapsState.None) {
            switchCapsState()
        }
    }

    private fun transformPopupPreview(c: String): String {
        if (c.length != 1) return c
        if (c[0].isLetter()) return transformAlphabet(c)
        return transformPunctuation(c)
    }

    override fun onPopupAction(action: PopupAction) {
        val newAction = when (action) {
            is PopupAction.PreviewAction -> action.copy(content = transformPopupPreview(action.content))
            is PopupAction.PreviewUpdateAction -> action.copy(content = transformPopupPreview(action.content))
            is PopupAction.ShowKeyboardAction -> {
                val label = action.keyboard.label
                if (label.length == 1 && label[0].isLetter())
                    action.copy(keyboard = KeyDef.Popup.Keyboard(transformAlphabet(label)))
                else action
            }
            else -> action
        }
        super.onPopupAction(newAction)
    }

    private fun switchCapsState(lock: Boolean = false) {
        capsState =
            if (lock) {
                when (capsState) {
                    CapsState.Lock -> CapsState.None
                    else -> CapsState.Lock
                }
            } else {
                when (capsState) {
                    CapsState.None -> CapsState.Once
                    else -> CapsState.None
                }
            }
        updateCapsButtonIcon()
        updateAlphabetKeys()
    }

    private fun updateCapsButtonIcon() {
        caps.img.apply {
            imageResource = when (capsState) {
                CapsState.None -> R.drawable.ic_capslock_none
                CapsState.Once -> R.drawable.ic_capslock_once
                CapsState.Lock -> R.drawable.ic_capslock_lock
            }
        }
    }

    private fun updateLangSwitchKey(visible: Boolean) {
        lang.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun updateAlphabetKeys() {
        textKeys.forEach {
            if (it.def !is KeyDef.Appearance.AltText) return
            it.mainText.text = it.def.displayText.let { str ->
                if (str.length != 1 || !str[0].isLetter()) return@forEach
                if (keepLettersUppercase) str.uppercase() else transformAlphabet(str)
            }

        }
    }

    private fun updatePunctuationKeys() {
        textKeys.forEach {
            if (it is AltTextKeyView) {
                it.def as KeyDef.Appearance.AltText
                it.altText.text = transformPunctuation(it.def.altText)
            } else {
                it.def as KeyDef.Appearance.Text
                it.mainText.text = it.def.displayText.let { str ->
                    if (str[0].run { isLetter() || isWhitespace() }) return@forEach
                    transformPunctuation(str)
                }
            }
        }
    }

}