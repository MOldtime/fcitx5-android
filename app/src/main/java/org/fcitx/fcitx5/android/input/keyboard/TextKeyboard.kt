/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2021-2023 Fcitx5 for Android Contributors
 */
package org.fcitx.fcitx5.android.input.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import androidx.annotation.Keep
import androidx.core.view.allViews
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxKeyMapping
import org.fcitx.fcitx5.android.core.InputMethodEntry
import org.fcitx.fcitx5.android.core.KeyState
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.KeySym
import org.fcitx.fcitx5.android.data.prefs.AppPrefs
import org.fcitx.fcitx5.android.data.prefs.ManagedPreference
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Appearance.Variant
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Behavior
import org.fcitx.fcitx5.android.input.keyboard.KeyDef.Popup
import org.fcitx.fcitx5.android.input.popup.PopupAction
import org.fcitx.fcitx5.android.input.popup.formContext
import splitties.views.imageResource

@SuppressLint("ViewConstructor")
class TextKeyboard(
    context: Context, theme: Theme
) : BaseKeyboard(context, theme, Layout) {

    enum class CapsState { None, Once, Lock }

    companion object {
        const val Name = "Text"

        val Layout: List<List<KeyDef>> = listOf(
            listOf(
                // @formatter:off
                AlphabetKeyNew("1", "!", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_1), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "!".uppercase(), default = false)))),
                AlphabetKeyNew("2", "@", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_2), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "@".uppercase(), default = false)))),
                AlphabetKeyNew("3", "#", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_3), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "#".uppercase(), default = false)))),
                AlphabetKeyNew("4", "$", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_4), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "$".uppercase(), default = false)))),
                AlphabetKeyNew("5", "%", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_5), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "%".uppercase(), default = false)))),
                AlphabetKeyNew("6", "^", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_6), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "^".uppercase(), default = false)))),
                AlphabetKeyNew("7", "&", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_7), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "&".uppercase(), default = false)))),
                AlphabetKeyNew("8", "*", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_8), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "*".uppercase(), default = false)))),
                AlphabetKeyNew("9", "(", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_9), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = "(".uppercase(), default = false)))),
                AlphabetKeyNew("0", ")", behavior = setOf(Behavior.Press(KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_0), KeyStates.Empty)), Behavior.Swipe(KeyAction.FcitxKeyAction(act = ")".uppercase(), default = false))))
                // @formatter:on
            ), listOf(
                AlphabetKeyNew("Q", "`"),
                AlphabetKeyNew("W", "~"),
                AlphabetKeyNew("E", "+"),
                AlphabetKeyNew("R", "-"),
                AlphabetKeyNew("T", "="),
                AlphabetKeyNew("Y", "_"),
                AlphabetKeyNew("U", "{"),
                AlphabetKeyNew("I", "}"),
                AlphabetKeyNew("O", "["),
                AlphabetKeyNew("P", "]")
            ), listOf(
                AlphabetKeyNew("A", "\\", percentWidth = 0.095f),
                AlphabetKeyNew("S", "|", percentWidth = 0.095f),
                AlphabetKeyNew("D", "×", percentWidth = 0.095f),
                AlphabetKeyNew("F", "÷", percentWidth = 0.095f),
                AlphabetKeyNew("G", "←", percentWidth = 0.095f),
                AlphabetKeyNew("H", "→", percentWidth = 0.095f),
                AlphabetKeyNew("J", formContext[7].component1(), percentWidth = 0.095f),
                AlphabetKeyNew("K", formContext[8].component1(), percentWidth = 0.095f),
                AlphabetKeyNew("L", formContext[9].component1(), percentWidth = 0.095f),
                AlphabetKeyNew(
                    ";",
                    ":",
                    percentWidth = 0.095f,
                    behavior = setOf(
                        Behavior.Press(KeyAction.FcitxKeyAction(";")),
                        Behavior.Swipe(KeyAction.FcitxKeyAction(":"))
                    )
                ),
            ), listOf(
                CapsKey(),
                AlphabetKeyNew("Z", formContext[0].component1()),
                AlphabetKeyNew("X", formContext[1].component1()),
                AlphabetKeyNew("C", formContext[2].component1()),
                AlphabetKeyNew("V", formContext[3].component1()),
                AlphabetKeyNew("B", formContext[4].component1()),
                AlphabetKeyNew("N", formContext[5].component1()),
                AlphabetKeyNew("M", formContext[6].component1()),
                BackspaceKey()
            ), listOf(
                // @formatter:off
                KeyDef(
                    Appearance.ImageText(" ", src = R.drawable.ic_baseline_tag_faces_24, textSize = 16f, textStyle = Typeface.NORMAL, percentWidth = 0.15f, variant = Variant.Alternative, viewId = R.id.button_number,), setOf( Behavior.Press(KeyAction.LayoutSwitchAction("")), Behavior.Swipe(KeyAction.PickerSwitchAction())),
                    arrayOf(
                        Popup.Menu(arrayOf(
                                Popup.Menu.Item("Emoji", R.drawable.ic_baseline_tag_faces_24, KeyAction.PickerSwitchAction()),
                                Popup.Menu.Item("QuickPhrase", R.drawable.ic_baseline_format_quote_24, KeyAction.QuickPhraseAction),
                                Popup.Menu.Item("Unicode", R.drawable.ic_logo_unicode, KeyAction.UnicodeAction)
                            ))
                    )),
                // @formatter:on
                LanguageKey(),
                AlphabetKey("/", "?"),
                AlphabetKey(",", "<"),
                SpaceKey(),
                AlphabetKey(".", ">"),
                AlphabetKey("'", "\""),
                ReturnKey()
            )
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
        buttonNumber.mainText.text = buildString { append("?123") }
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

    override fun onCandidateUpdate(status: Boolean) {
        super.onCandidateUpdate(status)
        caps.swipeEnabled = status
        caps.doubleTapEnabled = !status

        if (status) {
            caps.img.apply {
                imageResource = R.drawable.tab
            }

            caps.setOnClickListener {
                onAction(
                    KeyAction.SymAction(KeySym(FcitxKeyMapping.FcitxKey_Tab)),
                )
            }

        } else {
            updateCapsButtonIcon()
            caps.setOnClickListener {
                onAction(KeyAction.CapsAction(false))
            }
        }
    }

    override fun onPunctuationUpdate(mapping: Map<String, String>) {
        punctuationMapping = mapping
        updatePunctuationKeys()
    }

    override fun onInputMethodUpdate(ime: InputMethodEntry) {
        space.mainText.text = buildString {
            append(if (ime.label == "En") ime.name else ime.label)
            ime.subMode.run { name.ifEmpty { label.ifEmpty { null } } }?.let { append(" $it") }
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
//        lang.visibility = if (visible) View.VISIBLE else View.GONE
        lang.visibility = GONE
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
