/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2024-2025 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.input

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.ViewTreeObserver.OnPreDrawListener
import android.view.WindowInsets
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.Size
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxEvent
import org.fcitx.fcitx5.android.daemon.FcitxConnection
import org.fcitx.fcitx5.android.daemon.launchOnReady
import org.fcitx.fcitx5.android.data.InputFeedbacks
import org.fcitx.fcitx5.android.data.prefs.AppPrefs
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.candidates.floating.FloatingCandidatesPosition
import org.fcitx.fcitx5.android.input.candidates.floating.PagedCandidatesUi
import org.fcitx.fcitx5.android.input.preedit.PreeditUi
import org.fcitx.fcitx5.android.utils.item
import splitties.dimensions.dp
import splitties.resources.styledColor
import splitties.views.dsl.constraintlayout.below
import splitties.views.dsl.constraintlayout.bottomOfParent
import splitties.views.dsl.constraintlayout.centerHorizontally
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.constraintlayout.matchConstraints
import splitties.views.dsl.constraintlayout.startOfParent
import splitties.views.dsl.constraintlayout.topOfParent
import splitties.views.dsl.core.add
import splitties.views.dsl.core.withTheme
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import timber.log.Timber
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
class CandidatesView(
    service: FcitxInputMethodService,
    fcitx: FcitxConnection,
    theme: Theme
) : BaseInputView(service, fcitx, theme) {

    var isVirtualKeyboard = true

    private val ctx = context.withTheme(R.style.Theme_InputViewTheme)

    private val candidatesPrefs = AppPrefs.getInstance().candidates
    private val orientation by candidatesPrefs.orientation
    private val windowMinWidth by candidatesPrefs.windowMinWidth
    private val windowPadding by candidatesPrefs.windowPadding
    private val windowRadius by candidatesPrefs.windowRadius
    private val fontSize by candidatesPrefs.fontSize
    private val itemPaddingVertical by candidatesPrefs.itemPaddingVertical
    private val itemPaddingHorizontal by candidatesPrefs.itemPaddingHorizontal

    val floatingFollow by candidatesPrefs.floatingFollowPosition
    val floatingWindow by candidatesPrefs.floatingWindow

    private var inputPanel = FcitxEvent.InputPanelEvent.Data()
    private var paged = FcitxEvent.PagedCandidateEvent.Data.Empty

    /**
     * horizontal, bottom, top
     */
    private val anchorPosition = floatArrayOf(0f, 0f, 0f)
    private val parentSize = floatArrayOf(0f, 0f)

    private var shouldUpdatePosition = false

    /**
     * layout update may or may not cause [CandidatesView]'s size [onSizeChanged],
     * in either case, we should reposition it
     */
    private val layoutListener = OnGlobalLayoutListener {
        shouldUpdatePosition = true
    }

    /**
     * [CandidatesView]'s position is calculated based on it's size,
     * so we need to recalculate the position after layout,
     * and before any actual drawing to avoid flicker
     */
    private val preDrawListener = OnPreDrawListener {
        if (shouldUpdatePosition) {
            updatePosition()
        }
        true
    }

    private val touchEventReceiverWindow = TouchEventReceiverWindow(this)

    private val setupTextView: TextView.() -> Unit = {
        textSize = fontSize.toFloat()
        val v = dp(itemPaddingVertical)
        val h = dp(itemPaddingHorizontal)
        setPadding(h, v, h, v)
    }

    private val preeditUi = PreeditUi(ctx, theme, setupTextView)

    private val candidatesUi = PagedCandidatesUi(
        ctx, theme, setupTextView,
        onCandidateClick = { index -> fcitx.launchOnReady { it.select(index) } },
        onCandidateLongClick = { idx, text, ui ->
            fcitx.lifecycleScope.launch {
                val actions = fcitx.runOnReady { getCandidateActions(idx) }
                if (actions.isEmpty()) return@launch
                InputFeedbacks.hapticFeedback(ui, longPress = true)
                withContext(Dispatchers.Main) {
                    PopupMenu(context, ui).apply {
                        menu.add(buildSpannedString {
                            bold {
                                color(context.styledColor(android.R.attr.colorAccent)) {
                                    append(text)
                                }
                            }
                        }).apply {
                            isEnabled = false
                        }
                        actions.forEach { action ->
                            menu.item(action.text) {
                                fcitx.runIfReady { triggerCandidateAction(idx, action.id) }
                            }
                        }
                        show()
                    }
                }
            }
            true
        },
        onPrevPage = { fcitx.launchOnReady { it.offsetCandidatePage(-1) } },
        onNextPage = { fcitx.launchOnReady { it.offsetCandidatePage(1) } }
    )

    private var bottomInsets = 0

    override fun handleFcitxEvent(it: FcitxEvent<*>) {
        when (it) {
            is FcitxEvent.InputPanelEvent -> {
                inputPanel = it.data
                updateUi()
            }
            is FcitxEvent.PagedCandidateEvent -> {
                paged = it.data
                updateUi()
            }
            else -> {}
        }
    }

    private fun evaluateVisibility(): Boolean {
        return inputPanel.preedit.isNotEmpty() ||
                paged.candidates.isNotEmpty() ||
                inputPanel.auxUp.isNotEmpty() ||
                inputPanel.auxDown.isNotEmpty()
    }

    private fun updateUi() {
        preeditUi.update(inputPanel)
        preeditUi.root.visibility = if (preeditUi.visible) VISIBLE else GONE
        candidatesUi.update(paged, orientation)
        if (evaluateVisibility()) {
            visibility = VISIBLE
        } else {
            // RecyclerView won't update its items when ancestor view is GONE
            visibility = INVISIBLE
            touchEventReceiverWindow.dismiss()
        }
    }

    private fun updatePosition() {
        if (visibility != VISIBLE) {
            // skip unnecessary updates
            return
        }
        val (parentWidth, parentHeight) = parentSize
        if (parentWidth <= 0 || parentHeight <= 0) {
            // panic, bail
            translationX = 0f
            translationY = 0f
            return
        }
        val (horizontal, bottom, top) = anchorPosition
        val w: Int = width
        val h: Int = height
        Timber.d("updatePosition: horizontal: $horizontal, bottom: $bottom, top: $top, parentWidth: $parentWidth, parentHeight: $parentHeight")
        val selfWidth = w.toFloat()
        val selfHeight = h.toFloat()
        val tX: Float = if (floatingWindow || !isVirtualKeyboard) {
            when (floatingFollow) {
                FloatingCandidatesPosition.TopLeft, FloatingCandidatesPosition.BottomLeft -> {
                    5f
                }
                FloatingCandidatesPosition.TopRight, FloatingCandidatesPosition.BottomRight -> {
                    parentWidth - selfWidth - 5f
                }
                FloatingCandidatesPosition.Follow -> {
                    if (layoutDirection == LAYOUT_DIRECTION_RTL) {
                        val rtlOffset = parentWidth - horizontal
                        if (rtlOffset + selfWidth > parentWidth) selfWidth - parentWidth else -rtlOffset
                    } else {
                        if (horizontal + selfWidth > parentWidth) parentWidth - selfWidth else horizontal
                    }
                }
            }
        } else {
            5f
        }

        val tY: Float = if (isVirtualKeyboard) {
            when (floatingFollow) {
                FloatingCandidatesPosition.TopLeft, FloatingCandidatesPosition.TopRight -> {
                    if (top >= selfHeight) 0f else bottom
                }
                FloatingCandidatesPosition.BottomLeft, FloatingCandidatesPosition.BottomRight -> {
                    if (bottom + selfHeight + 5f <= parentHeight) parentHeight - selfHeight - 5f else (if (top < parentHeight) top else parentHeight) - selfHeight - 5f
                }
                FloatingCandidatesPosition.Follow -> {
                    if (bottom + selfHeight + 5f <= parentHeight) bottom else (if (top < parentHeight) top else parentHeight) - selfHeight - 5f
                }
            }
        } else {
            // 外接
            val height = height.toFloat()
            val bottomCoordinate = bottom + selfHeight
            if (bottomCoordinate < height) /*放下面*/ bottomCoordinate else (if (top < height) top else height) - selfHeight
        }

        translationX = tX
        translationY = tY
        // update touchEventReceiverWindow's position after CandidatesView's
        touchEventReceiverWindow.showAt(tX.roundToInt(), tY.roundToInt(), w, h)
        shouldUpdatePosition = false
    }

    fun updateCursorAnchor(@Size(4) anchor: FloatArray, @Size(2) parent: FloatArray) {
        val (horizontal, bottom, _, top) = anchor
        val (parentWidth, parentHeight) = parent
        anchorPosition[0] = horizontal
        anchorPosition[1] = bottom
        anchorPosition[2] = top
        if (parentWidth > 0)
            parentSize[0] = parentWidth
        if (parentHeight > 0)
            parentSize[1] = parentHeight
        updatePosition()
    }

    fun setCursorAnchor(height: Float) {
        if (height <= 0f) return
        anchorPosition[0] = 0f
        anchorPosition[1] = height
        anchorPosition[2] = height
        Timber.d("setCursorAnchor: height: $height")
    }

    fun setParentSize(width: Int, y: Float) {
        if (width <= 0 || y <= 0f) return
        parentSize[0] = width.toFloat()
        parentSize[1] = y
        Timber.d("setParentSize: width: ${parentSize[0]}, height: ${parentSize[1]}")
    }

    fun clean() {
        handleEvents = false
        touchEventReceiverWindow.dismiss()
    }

    init {
        // invisible by default
        visibility = INVISIBLE

        minWidth = dp(windowMinWidth)
        padding = dp(windowPadding)
        background = GradientDrawable().apply {
            setColor(theme.backgroundColor)
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dp(windowRadius).toFloat()
        }
        clipToOutline = true
        outlineProvider = ViewOutlineProvider.BACKGROUND
        add(preeditUi.root, lParams(wrapContent, wrapContent) {
            topOfParent()
            startOfParent()
        })
        add(candidatesUi.root, lParams(matchConstraints, wrapContent) {
            matchConstraintMinWidth = wrapContent
            below(preeditUi.root)
            centerHorizontally()
            bottomOfParent()
        })

        isFocusable = false
        layoutParams = ViewGroup.LayoutParams(wrapContent, wrapContent)
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            bottomInsets = getNavBarBottomInset(insets)
        }
        return insets
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
        viewTreeObserver.addOnPreDrawListener(preDrawListener)
    }

    override fun onDetachedFromWindow() {
        viewTreeObserver.removeOnPreDrawListener(preDrawListener)
        viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
        touchEventReceiverWindow.dismiss()
        super.onDetachedFromWindow()
    }
}
