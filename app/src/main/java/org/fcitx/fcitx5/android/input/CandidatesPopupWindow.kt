package org.fcitx.fcitx5.android.input

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.Gravity
import android.view.View.LAYOUT_DIRECTION_RTL
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.Size
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.FcitxEvent
import org.fcitx.fcitx5.android.daemon.FcitxConnection
import org.fcitx.fcitx5.android.data.prefs.AppPrefs
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.input.candidates.floating.FloatingCandidatesPosition
import org.fcitx.fcitx5.android.input.candidates.floating.PagedCandidatesUi
import org.fcitx.fcitx5.android.input.preedit.PreeditUi
import splitties.dimensions.dp
import splitties.views.dsl.constraintlayout.below
import splitties.views.dsl.constraintlayout.bottomOfParent
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.constraintlayout.startOfParent
import splitties.views.dsl.constraintlayout.topOfParent
import splitties.views.dsl.core.add
import splitties.views.dsl.core.withTheme
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import splitties.views.verticalPadding
import timber.log.Timber
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3

class CandidatesPopupWindow(
    val parentView: FrameLayout,
    service: FcitxInputMethodService,
    fcitx: FcitxConnection,
    theme: Theme
) : PopupWindow() {
    var candidates = CandidatesView(service, fcitx, theme)
        private set
    private var layout = false
    var isVirtualKeyboard = true

    init {
        contentView = candidates
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        parentView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 移除监听器，防止多次调用
                parentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                layout = true
            }
        })
//        animationStyle = R.style.PopupAnimation
    }

    /**
     * horizontal, bottom, top
     */
    private val anchorPosition = floatArrayOf(0f, 0f, 0f)
    private val parentSize = floatArrayOf(0f, 0f)

    private fun show(x: Float, y: Float) = show(x.toInt(), y.toInt())
    private fun show(x: Int, y: Int) {
        if (isShowing) {
            update(x, y, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            return
        }
        if (layout && candidates.visibilitY) {
            contentView = candidates
            showAtLocation(parentView, Gravity.NO_GRAVITY, x, y)
            return
        }
    }

    private fun hide() {
        contentView = null
        val handleEvents = candidates.handleEvents
        dismiss()
        candidates.handleEvents = handleEvents
    }

    private fun updatePosition() {
        val (horizontal, bottom, top) = anchorPosition
        val (parentWidth, parentHeight) = parentSize
        Timber.d("updatePosition: horizontal: $horizontal, bottom: $bottom, top: $top, parentWidth: $parentWidth, parentHeight: $parentHeight")
        val selfWidth = candidates.width.toFloat()
        val selfHeight = candidates.height.toFloat()
        val x =
            if (candidates.floatingWindow || !isVirtualKeyboard) {
                when (candidates.floatingFollow) {
                    FloatingCandidatesPosition.TopLeft, FloatingCandidatesPosition.BottomLeft -> {
                        5f
                    }
                    FloatingCandidatesPosition.TopRight, FloatingCandidatesPosition.BottomRight -> {
                        parentWidth - selfWidth - 5f
                    }
                    FloatingCandidatesPosition.Follow -> {
                        if (candidates.layoutDirection == LAYOUT_DIRECTION_RTL) {
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

        val y = if (isVirtualKeyboard) {
            when (candidates.floatingFollow) {
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
            val height = parentView.height.toFloat()
            val bottomCoordinate = bottom + selfHeight
            if (bottomCoordinate < height) /*放下面*/ bottomCoordinate else (if (top < height) top else height) - selfHeight
        }
        show(x, y)
    }

    fun updateCursorAnchor(@Size(4) anchor: FloatArray, @Size(2) parent: FloatArray) {
        val (horizontal, bottom, _, top) = anchor
        val (parentWidth, parentHeight) = parent
        Timber.d("updateCursorAnchor: horizontal: $horizontal, bottom: $bottom, top: $top, parentWidth: $parentWidth, parentHeight: $parentHeight")
        anchorPosition[0] = horizontal
        anchorPosition[1] = bottom
        anchorPosition[2] = top
        if (parentWidth > 0)
            parentSize[0] = parentWidth
        if (parentHeight > 0)
            parentSize[1] = parentHeight
        updatePosition()
    }

    fun clean() {
        candidates.handleEvents = false
        dismiss()
        contentView = null
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
        parentSize[1] = y.toFloat()
        Timber.d("setParentSize: width: ${parentSize[0]}, height: ${parentSize[1]}")
    }


    fun reset(
        service: FcitxInputMethodService,
        fcitx: FcitxConnection,
        theme: Theme
    ) {
        clean()
        candidates = CandidatesView(service, fcitx, theme)
        contentView = candidates
    }

    /**
     * Quote upstream content [org.fcitx.fcitx5.android.input.CandidatesView]
     */
    @SuppressLint("ViewConstructor")
    inner class CandidatesView(
        service: FcitxInputMethodService,
        fcitx: FcitxConnection,
        theme: Theme
    ) : BaseInputView(service, fcitx, theme) {
        var visibilitY = false
            private set
        private val ctx = context.withTheme(R.style.Theme_InputViewTheme)
        private val candidatesPrefs = AppPrefs.getInstance().candidates
        private val orientation by candidatesPrefs.orientation
        private val windowMinWidth by candidatesPrefs.windowMinWidth
        private val windowPadding by candidatesPrefs.windowPadding
        private val fontSize by candidatesPrefs.fontSize
        private val itemPaddingVertical by candidatesPrefs.itemPaddingVertical
        private val itemPaddingHorizontal by candidatesPrefs.itemPaddingHorizontal
        val floatingFollow by candidatesPrefs.floatingFollowPosition
        val floatingWindow by candidatesPrefs.floatingWindow
        private var inputPanel = FcitxEvent.InputPanelEvent.Data()
        private var paged = FcitxEvent.PagedCandidateEvent.Data.Empty
        private var currentPage = -1
        private var currentTotal = -1

        private val setupTextView: TextView.() -> Unit = {
            textSize = fontSize.toFloat()
            val v = dp(itemPaddingVertical)
            val h = dp(itemPaddingHorizontal)
            setPadding(h, v, h, v)
        }

        private val preeditUi = PreeditUi(ctx, theme, setupTextView)

        private val candidatesUi = PagedCandidatesUi(ctx, theme, fcitx, service, setupTextView)

        private var bottomInsets = 0


        override fun handleFcitxEvent(it: FcitxEvent<*>) {
            when (it) {
                is FcitxEvent.InputPanelEvent -> {
                    inputPanel = it.data
                    updateUi()
                }
                is FcitxEvent.PagedCandidateEvent -> {
                    if (it.data.currentPage == 0) {
                        this.currentTotal = it.data.candidates.size
                    }
                    this.currentPage = it.data.currentPage
                    paged = it.data
                    updateUi()
                }
                else -> {}
            }
        }

        fun evaluateVisibility(): Boolean {
            return inputPanel.preedit.isNotEmpty() ||
                    paged.candidates.isNotEmpty() ||
                    inputPanel.auxUp.isNotEmpty() ||
                    inputPanel.auxDown.isNotEmpty()
        }

        private fun updateUi() {
            if (evaluateVisibility()) {
                preeditUi.update(inputPanel)
                preeditUi.root.visibility = if (preeditUi.visible) VISIBLE else GONE
                candidatesUi.update(paged, orientation)
                selfMeasure()
                visibilitY = true
//                resetCursorAnchor()
                updatePosition()
            } else {
                visibilitY = false
                hide()
            }
        }

        init {
            // invisible by default
            verticalPadding = dp(4)
            minWidth = dp(windowMinWidth)
            padding = dp(windowPadding)
            background = GradientDrawable().apply {
                setColor(theme.backgroundColor)
                setCornerRadius(16f) // 圆角半径
            }
            add(preeditUi.root, lParams(wrapContent, wrapContent) {
                topOfParent()
                startOfParent()
            })
            add(candidatesUi.root, lParams(wrapContent, wrapContent) {
                below(preeditUi.root)
                startOfParent()
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

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            if (widthMeasureSpec == -1 && heightMeasureSpec == -1) {
                super.onMeasure(
                    MeasureSpec.makeMeasureSpec(
                        this@CandidatesPopupWindow.parentView.width,
                        MeasureSpec.AT_MOST
                    ), MeasureSpec.makeMeasureSpec(
                        this@CandidatesPopupWindow.parentView.height,
                        MeasureSpec.AT_MOST
                    )
                )
                return
            }
            setMeasuredDimension(measuredWidth, measuredHeight)
        }

        fun selfMeasure() {
            measure(-1, -1)
            layout(0, 0, measuredWidth, measuredHeight)
        }
    }
}