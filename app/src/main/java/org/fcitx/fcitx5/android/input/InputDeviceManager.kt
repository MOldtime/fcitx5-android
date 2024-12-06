/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2024 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input

import android.text.InputType
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import org.fcitx.fcitx5.android.data.prefs.AppPrefs
import org.fcitx.fcitx5.android.input.candidates.floating.FloatingCandidatesMode
import org.fcitx.fcitx5.android.utils.monitorCursorAnchor
import timber.log.Timber

class InputDeviceManager(private val onChange: (Boolean) -> Unit) {

    private var inputView: InputView? = null
    private var candidatesView: CandidatesPopupWindow? = null
    val isShowCandidate by AppPrefs.getInstance().candidates.floatingWindow
    private val isHideCandidate by AppPrefs.getInstance().candidates.hideCandidates
    private var pagingMode = 0

    private fun setupInputViewEvents(isVirtual: Boolean) {
        inputView?.handleEvents = isVirtual
        if (isVirtual) {
            inputView?.apply {
                this.viewTreeObserver.addOnPreDrawListener(object :
                    ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        Timber.d("onPreDraw ${(this@InputDeviceManager.candidatesView == null)}")
                        // 移除监听器，防止多次调用
                        this@apply.viewTreeObserver.removeOnPreDrawListener(this)
                        this@InputDeviceManager.candidatesView?.setParentSize(
                            this@apply.keyboardView.width, this@apply.keyboardView.y
                        )
                        this@InputDeviceManager.candidatesView?.setCursorAnchor(this@apply.keyboardView.y)
                        return true
                    }
                })
            }
        }
        inputView?.visibility = if (isVirtual) View.VISIBLE else View.GONE
    }

    private fun setupCandidatesViewEvents(isVirtual: Boolean) {
        if (!isVirtual) {
            candidatesView?.candidates?.handleEvents = true
            return
        }
        if (isHideCandidate) {
            candidatesView?.clean()
            return
        }
        if (isShowCandidate) {
            candidatesView?.candidates?.handleEvents = true
        } else {
            candidatesView?.clean()
        }
    }

    private fun setupViewEvents(isVirtual: Boolean) {
        setupInputViewEvents(isVirtual)
        setupCandidatesViewEvents(isVirtual)
    }

    var isVirtualKeyboard = true
        private set(value) {
            field = value
            candidatesView?.isVirtualKeyboard = value
            setupViewEvents(value)
        }

    fun setInputView(inputView: InputView) {
        this.inputView = inputView
        setupInputViewEvents(this.isVirtualKeyboard)
    }

    fun setCandidatesView(candidatesView: CandidatesPopupWindow) {
        this.candidatesView = candidatesView
        setCandidatesView()
    }

    fun setCandidatesView() {
        setupCandidatesViewEvents(this.isVirtualKeyboard)
    }

    private fun applyMode(
        service: FcitxInputMethodService,
        useVirtualKeyboard: Boolean,
    ) {
        // TODO 待优化
        Timber.d("applyMode useVirtualKeyboard: $useVirtualKeyboard, isVirtualKeyboard: $isVirtualKeyboard, isHideCandidate: $isHideCandidate, isShowCandidate: $isShowCandidate")
        service.currentInputConnection?.monitorCursorAnchor(if (!useVirtualKeyboard) true else if (isHideCandidate) false else isShowCandidate)
            ?: Timber.d("applyMode service.currentInputConnection is null")
        val pagingMode_ =
            if ((!useVirtualKeyboard) || isShowCandidate || (isHideCandidate)) 1 else 0
        if (pagingMode_ != pagingMode) {
            pagingMode = pagingMode_
            service.postFcitxJob {
                setCandidatePagingMode(pagingMode_)
            }
        }
        if (useVirtualKeyboard != isVirtualKeyboard) isVirtualKeyboard = useVirtualKeyboard
        onChange(isVirtualKeyboard)
    }

    private var startedInputView = false
    private var isNullInputType = true

    private var candidatesViewMode by AppPrefs.getInstance().candidates.mode

    /**
     * @return should use virtual keyboard
     */
    fun evaluateOnStartInputView(info: EditorInfo, service: FcitxInputMethodService): Boolean {
        Timber.d("evaluateOnStartInputView")
        startedInputView = true
        isNullInputType = info.inputType and InputType.TYPE_MASK_CLASS == InputType.TYPE_NULL
        val useVirtualKeyboard = when (candidatesViewMode) {
            FloatingCandidatesMode.SystemDefault -> service.superEvaluateInputViewShown()
            FloatingCandidatesMode.InputDevice -> isVirtualKeyboard
            FloatingCandidatesMode.Disabled -> true
        }
        applyMode(service, useVirtualKeyboard)
        // 针对没有汇报光标的软件做初始化
        candidatesView?.setCursorAnchor(inputView?.keyboardView?.y ?: 0f)
        return useVirtualKeyboard
    }

    /**
     * @return should force show input views
     */
    fun evaluateOnKeyDown(e: KeyEvent, service: FcitxInputMethodService): Boolean {
        if (startedInputView) {
            // filter out back/home/volume buttons
            if (e.isPrintingKey) {
                // evaluate virtual keyboard visibility when pressing physical keyboard while InputView visible
                evaluateOnKeyDownInner(service)
            }
            // no need to force show InputView since it's already visible
            return false
        } else {
            // force show InputView when focusing on text input (likely inputType is not TYPE_NULL)
            // and pressing any digit/letter/punctuation key on physical keyboard
            val showInputView = !isNullInputType && e.isPrintingKey
            if (showInputView) {
                evaluateOnKeyDownInner(service)
            }
            return showInputView
        }
    }

    private fun evaluateOnKeyDownInner(service: FcitxInputMethodService) {
        val useVirtualKeyboard = when (candidatesViewMode) {
            FloatingCandidatesMode.SystemDefault -> service.superEvaluateInputViewShown()
            FloatingCandidatesMode.InputDevice -> false
            FloatingCandidatesMode.Disabled -> true
        }
        applyMode(service, useVirtualKeyboard)
    }

    fun evaluateOnViewClicked(service: FcitxInputMethodService) {
        if (!startedInputView) return
        val useVirtualKeyboard = when (candidatesViewMode) {
            FloatingCandidatesMode.SystemDefault -> service.superEvaluateInputViewShown()
            else -> true
        }
        applyMode(service, useVirtualKeyboard)
    }

    fun evaluateOnUpdateEditorToolType(toolType: Int, service: FcitxInputMethodService) {
        if (!startedInputView) return
        val useVirtualKeyboard = when (candidatesViewMode) {
            FloatingCandidatesMode.SystemDefault -> service.superEvaluateInputViewShown()
            FloatingCandidatesMode.InputDevice ->
                // switch to virtual keyboard on touch screen events, otherwise preserve current mode
                if (toolType == MotionEvent.TOOL_TYPE_FINGER || toolType == MotionEvent.TOOL_TYPE_STYLUS) true else isVirtualKeyboard
            FloatingCandidatesMode.Disabled -> true
        }
        applyMode(service, useVirtualKeyboard)
    }

    fun onFinishInputView() {
        startedInputView = false
    }
}
