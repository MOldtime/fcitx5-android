/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2023 Fcitx5 for Android Contributors
 */
package org.fcitx.fcitx5.android.input.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.View
import androidx.annotation.Keep
import androidx.core.view.allViews
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxKeyMapping
import org.fcitx.fcitx5.android.core.InputMethodEntry
import org.fcitx.fcitx5.android.core.KeyState
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.KeySym
import org.fcitx.fcitx5.android.core.ScancodeMapping
import org.fcitx.fcitx5.android.data.prefs.AppPrefs
import org.fcitx.fcitx5.android.data.prefs.ManagedPreference
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.clipboard.ClipboardWindow
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance.Variant
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Behavior
import org.fcitx.fcitx5.android.input.popup.PopupAction
import splitties.views.imageResource
import splitties.views.onClick

@SuppressLint("ViewConstructor")
class TextKeyboard(
    context: Context, theme: Theme
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
                AlphabetKeyNew("1", "!", percentWidth = 1f),
                AlphabetKeyNew("2", "@", percentWidth = 1f),
                AlphabetKeyNew("3", "#", percentWidth = 1f),
                AlphabetKeyNew("4", "$", percentWidth = 1f),
                AlphabetKeyNew("5", "%", percentWidth = 1f),
                AlphabetKeyNew("6", "^", percentWidth = 1f),
                AlphabetKeyNew("7", "&", percentWidth = 1f),
                AlphabetKeyNew("8", "*", percentWidth = 1f),
                AlphabetKeyNew("9", "(", percentWidth = 1f),
                AlphabetKeyNew("0", ")", percentWidth = 1f)
            ), listOf(
                AlphabetKeyNew("Q", "`", percentWidth = 1f),
                AlphabetKeyNew("W", "~", percentWidth = 1f),
                AlphabetKeyNew("E", "+", percentWidth = 1f),
                AlphabetKeyNew("R", "-", percentWidth = 1f),
                AlphabetKeyNew("T", "=", percentWidth = 1f),
                AlphabetKeyNew("Y", "_", percentWidth = 1f),
                AlphabetKeyNew("U", "<", percentWidth = 1f),
                AlphabetKeyNew("I", ">", percentWidth = 1f),
                AlphabetKeyNew("O", "[", percentWidth = 1f),
                AlphabetKeyNew("P", "]", percentWidth = 1f)
            ), listOf(
                AlphabetKeyNew("A", "\\", percentWidth = 0.095f),
                AlphabetKeyNew("S", "|", percentWidth = 0.095f),
                AlphabetKeyNew("D", "×", percentWidth = 0.095f),
                AlphabetKeyNew("F", "÷", percentWidth = 0.095f),
                AlphabetKeyNew("G", "←", percentWidth = 0.095f),
                AlphabetKeyNew("H", "→", percentWidth = 0.095f),
                AlphabetKeyNew("J", "↑", percentWidth = 0.095f),
                AlphabetKeyNew("K", "↓", percentWidth = 0.095f),
                AlphabetKeyNew("L", "/", percentWidth = 0.095f),
                AlphabetKeyNew(
                    ";", ":", percentWidth = 0.095f, behavior = setOf(
                        Behavior.Press(KeyAction.FcitxKeyAction(";")),
                        Behavior.Swipe(KeyAction.FcitxKeyAction(":")),
                    )
                ),
            ), listOf(
                CapsKey(), KeyDef(
                    Appearance.AltText(
                        displayText = "Z",
                        altText = "全选",
                        textSize = 23f,
                        variant = Appearance.Variant.Normal
                    ),
                    setOf(
                        Behavior.LongPress(KeyAction.PerformContextMenuAction(android.R.id.selectAll)),
                        Behavior.Press(KeyAction.FcitxKeyAction("Z")),
                        Behavior.Swipe(KeyAction.FcitxKeyAction("Z", default = false)),
                    ),
                ), KeyDef(
                    Appearance.AltText(
                        displayText = "X",
                        altText = "剪切",
                        textSize = 23f,
                        variant = Appearance.Variant.Normal
                    ), setOf(
                        Behavior.LongPress(KeyAction.PerformContextMenuAction(android.R.id.cut)),
                        Behavior.Press(KeyAction.FcitxKeyAction("X")),
                        Behavior.Swipe(KeyAction.FcitxKeyAction("X", default = false)),
                    )
                ), KeyDef(
                    Appearance.AltText(
                        displayText = "C",
                        altText = "复制",
                        textSize = 23f,
                        variant = Appearance.Variant.Normal
                    ),
                    setOf(
                        Behavior.LongPress(KeyAction.PerformContextMenuAction(android.R.id.copy)),
                        Behavior.Press(KeyAction.FcitxKeyAction("C")),
                        Behavior.Swipe(KeyAction.FcitxKeyAction("C", default = false)),
                    ),
                ), KeyDef(
                    Appearance.AltText(
                        displayText = "V",
                        altText = "粘贴",
                        textSize = 23f,
                        variant = Appearance.Variant.Normal
                    ),
                    setOf(
                        Behavior.LongPress(KeyAction.PerformContextMenuAction(android.R.id.paste)),
                        Behavior.Press(KeyAction.FcitxKeyAction("V")),
                        Behavior.Swipe(KeyAction.FcitxKeyAction("V", default = false)),
                    ),
                ), KeyDef(
                    Appearance.AltText(
                        displayText = "B",
                        altText = "剪贴",
                        textSize = 23f,
                        variant = Appearance.Variant.Normal
                    ),
                    setOf(
                        Behavior.LongPress(KeyAction.attachWindow(ClipboardWindow())),
                        Behavior.Press(KeyAction.FcitxKeyAction("B")),
                        Behavior.Swipe(KeyAction.FcitxKeyAction("B", default = false)),
                    ),
                ), AlphabetKeyNew(
                    "N", "翻转", percentWidth = 1f, behavior = setOf(
                        Behavior.Press(KeyAction.FcitxKeyAction("N")), Behavior.LongPress(
                            KeyAction.SymAction(
                                KeySym(FcitxKeyMapping.FcitxKey_Return),
                                KeyStates(KeyState.Virtual, KeyState.Shift)
                            )
                        ), Behavior.Swipe(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_N)))
                    ), popup = arrayOf()
                ), AlphabetKeyNew(
                    "M", "大写", percentWidth = 1f, behavior = setOf(
                        Behavior.Press(KeyAction.FcitxKeyAction("M")), Behavior.LongPress(
                            KeyAction.SymAction(
                                KeySym(FcitxKeyMapping.FcitxKey_Return),
                                KeyStates(KeyState.Virtual, KeyState.Ctrl)
                            )
                        ), Behavior.Swipe(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_M)))
                    ), popup = arrayOf()
                ), BackspaceKey()
            ), listOf(
                KeyDef(
                    Appearance.Text(
                        "?123",
                        textSize = 16f,
                        textStyle = Typeface.BOLD,
                        percentWidth = 0.15f,
                        variant = Variant.Alternative,
                        viewId = R.id.button_number,
                    ),
                    setOf(
                        Behavior.Press(KeyAction.LayoutSwitchAction("")),
                    ),
                ),
                Emoji(),
                LanguageKey(),
                SpaceKey(),
                AlphabetKey(",", "."),
                AlphabetKey("'", "\""),
                ReturnKey()
            )
//            **/
        )
    }

    val buttonNumber: TextKeyView by lazy { findViewById(R.id.button_number) }

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
        caps.swipeEnabled = false
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
                            if (action.default) {
                                transformed = action.copy(act = action.act.lowercase())
                            }
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
        caps.swipeEnabled = status
        caps.doubleTapEnabled = !status
        lang.swipeEnabled = status
        if (status) {
            caps.setOnClickListener {
                onAction(
                    KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_Tab)),
                )
            }

            buttonNumber.setOnClickListener {
                onAction(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_Escape)))
            }

            buttonNumber.setOnLongClickListener {
                onAction(KeyAction.LayoutSwitchAction(""))
                true
            }

            lang.swipeRewrite(
                KeyAction.PageAction(Page.PageUP)
            )

            lang.setOnClickListener {
                onAction(KeyAction.PageAction())
            }

            space.swipeRewrite(
                KeyAction.FcitxKeyAction(
                    "Space", ScancodeMapping.KEY_SPACE, KeyStates(KeyState.Virtual, KeyState.Shift)
                )
            )

        } else {
            caps.setOnClickListener {
                onAction(KeyAction.CapsAction(false))
            }

            buttonNumber.setOnClickListener {
                onAction(KeyAction.LayoutSwitchAction(""))
            }

            buttonNumber.setOnLongClickListener {
                true
            }
            lang.setOnClickListener {
                onAction(KeyAction.LangSwitchAction)
            }

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
//            if (ime.uniqueName != "rime" && ime.subMode.label != "A") {
            switchCapsState()
//            }
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
                if (label.length == 1 && label[0].isLetter()) action.copy(
                    keyboard = KeyDef.Popup.Keyboard(
                        transformAlphabet(label)
                    )
                )
                else action
            }
            else -> action
        }
        super.onPopupAction(newAction)
    }

    private fun switchCapsState(lock: Boolean = false) {
        capsState = if (lock) {
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
