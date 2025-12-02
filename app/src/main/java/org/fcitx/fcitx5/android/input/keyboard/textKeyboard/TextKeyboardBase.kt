/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2025 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input.keyboard.textKeyboard

import android.content.Context
import androidx.core.view.allViews
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxKeyMapping
import org.fcitx.fcitx5.android.core.InputMethodEntry
import org.fcitx.fcitx5.android.core.KeyState
import org.fcitx.fcitx5.android.core.KeyStates
import org.fcitx.fcitx5.android.core.KeySym
import org.fcitx.fcitx5.android.data.prefs.AppPrefs
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.keyboard.AltTextKeyView
import org.fcitx.fcitx5.android.input.keyboard.BaseKeyboard
import org.fcitx.fcitx5.android.input.keyboard.ImageKeyView
import org.fcitx.fcitx5.android.input.keyboard.KeyAction
import org.fcitx.fcitx5.android.input.keyboard.KeyActionListener
import org.fcitx.fcitx5.android.input.keyboard.KeyDef
import org.fcitx.fcitx5.android.input.keyboard.TextKeyView
import org.fcitx.fcitx5.android.input.popup.PopupAction
import splitties.views.imageResource

abstract class TextKeyboardBase(
    context: Context, theme: Theme, keyLayout: List<List<KeyDef>>
) : BaseKeyboard(context, theme, keyLayout) {
    enum class CapsState { None, Once, Lock }

    protected val keepLettersUppercase by AppPrefs.getInstance().keyboard.keepLettersUppercase
    protected val enExcluded by AppPrefs.getInstance().keyboard.enExcluded

    protected var languageCode: String? = null
    protected var capsState: CapsState = CapsState.None
    protected val textKeys: List<TextKeyView> by lazy {
        allViews.filterIsInstance(TextKeyView::class.java).toList()
    }

    val caps: ImageKeyView by lazy { findViewById(R.id.button_caps) }
    val space: TextKeyView by lazy { findViewById(R.id.button_space) }
    val `return`: ImageKeyView by lazy { findViewById(R.id.button_return) }

    val buttonNumber: TextKeyView by lazy { findViewById(R.id.button_number) }

//    val backspace: ImageKeyView by lazy { findViewById(R.id.button_backspace) }
//    val quickphrase: ImageKeyView by lazy { findViewById(R.id.button_quickphrase) }

    init {
        caps.swipeEnabled = false
        buttonNumber.mainText.text = buildString {
            append("?123")
        }
    }

    protected var punctuationMapping: Map<String, String> = mapOf()
    protected fun transformPunctuation(p: String) = punctuationMapping.getOrDefault(p, p)

    protected open fun transformAlphabet(c: String): String {
        return when (capsState) {
            CapsState.None -> c.lowercase()
            else -> c.uppercase()
        }
    }

    override fun onReturnDrawableUpdate(returnDrawable: Int) {
        `return`.img.imageResource = returnDrawable
    }

    override fun onAttach() {
        capsState = CapsState.None
        updateCapsButtonIcon()
        updateAlphabetKeys()
    }

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

    protected open fun switchCapsState(lock: Boolean = false) {
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

    protected open fun updateCapsButtonIcon() {
        caps.img.apply {
            imageResource = when (capsState) {
                CapsState.None -> R.drawable.ic_capslock_none
                CapsState.Once -> R.drawable.ic_capslock_once
                CapsState.Lock -> R.drawable.ic_capslock_lock
            }
        }
    }

    protected open fun updateAlphabetKeys() {
        val uppercase =
            keepLettersUppercase && if (enExcluded) this.languageCode != null && this.languageCode != "en" else true
        textKeys.forEach {
            if (it.def !is KeyDef.Appearance.AltText) return
            it.mainText.text = it.def.displayText.let { str ->
                if (str.length != 1 || !str[0].isLetter()) return@forEach
                if (uppercase) {
                    str.uppercase()
                } else transformAlphabet(str)
            }
        }
    }

    protected fun updatePunctuationKeys() {
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

    protected fun transformPopupPreview(c: String): String {
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
}