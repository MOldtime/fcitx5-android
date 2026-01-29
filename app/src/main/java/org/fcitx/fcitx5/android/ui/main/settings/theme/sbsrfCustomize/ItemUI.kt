/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import org.fcitx.fcitx5.android.R
import splitties.resources.color
import splitties.views.onClick
import splitties.views.onLongClick

class ItemUI(context: Context) : LinearLayout(context) {
    lateinit var fileToken: String
    lateinit var fileVersionView: TextView
    lateinit var sizeView: TextView
    lateinit var downloadButton: Button
    lateinit var channelTagView: TextView
    var url: String? = null
    var restore: Boolean = false

    var status = Status.IDLE
        private set

    init {
        orientation = VERTICAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        setupViews()
    }

    // 由ai编写完成
    private fun setupViews() {
        val container = ConstraintLayout(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            setPadding(20, 20, 20, 10)
        }

        val leftColumn = LinearLayout(context).apply {
            id = generateViewId()
            orientation = VERTICAL
            fileVersionView = TextView(context).apply {
                textSize = 18f
                setTextColor(color(R.color.sbxlm_version_text))
            }
            addView(fileVersionView)
            val row = LinearLayout(context).apply {
                sizeView = TextView(context).apply {
                    textSize = 14f
                    setTextColor(color(R.color.sbxlm_size_text))
                }
                channelTagView = TextView(context).apply {
                    layoutParams = LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                    ).apply {
                        leftMargin = dp(3)
                    }
                    textSize = 14f
                    setPadding(10, 0, 10, 0)
                }
                addView(sizeView)
                addView(channelTagView)
            }
            addView(row)
        }
        container.addView(leftColumn)

        val separator = View(context).apply {
            id = generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(2, 0) // 高度由约束决定
            background = ShapeDrawable(RectShape()).apply {
                paint.color = Color.LTGRAY
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 2f
                paint.pathEffect = DashPathEffect(floatArrayOf(8f, 6f), 0f)
            }
        }
        container.addView(separator)

        downloadButton = Button(context).apply {
//            layoutParams = LayoutParams(
//                LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT
//            ).apply {
//                topMargin = 10
//            }
            id = generateViewId()
            textSize = 15f
            setTextColor(color(R.color.sbxlm_download_text))
            background = GradientDrawable().apply {
                cornerRadius = 20f
                setColor(color(R.color.sbxlm_download_background))
            }
        }
        container.addView(downloadButton)

        val set = ConstraintSet()
        set.clone(container)

        set.connect(
            leftColumn.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            20
        )
        set.connect(leftColumn.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(
            leftColumn.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )

        set.connect(
            downloadButton.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            20
        )
        set.connect(
            downloadButton.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        set.connect(
            downloadButton.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )

        set.centerHorizontally(separator.id, ConstraintSet.PARENT_ID)
        set.connect(separator.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(
            separator.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        set.constrainWidth(separator.id, 2) // 固定宽
        set.applyTo(container)
        addView(container)
    }

    fun View.dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    fun bindData(
        position: Int,
        data: ReleaseData,
        restore: ItemUI.(data: ReleaseData, position: Int) -> Unit,
        onClick: ItemUI.(data: ReleaseData, position: Int) -> Unit,
        onLongClick: () -> Unit,
    ) {
        fileToken = data.fileToken
        fileVersionView.text = data.fileVersion
        sizeView.text = data.convertSize()
        setChannelTagView(data)
        restore(data, position)
        downloadButton.onClick {
            onClick(data, position)
        }
        downloadButton.onLongClick {
            onLongClick()
        }
    }

    private fun setChannelTagView(data: ReleaseData) = channelTagView.apply {
        val color = Color.parseColor(if (data.preRelease) "#FF836936" else "#FF297640")
        text = if (data.preRelease) "预发布" else "正式版"
        setTextColor(color)
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setStroke(dp(1), color)
            cornerRadius = 15f
            setColor(Color.TRANSPARENT)
        }
    }

    fun setProgress(progress: Int) {
        downloadButton.text = "$progress %"
    }

    fun setDownloadButtonStatus(newStatus: Status) {
        status = newStatus
        when (status) {
            Status.IDLE -> {
                downloadButton.text = "下载"
            }
            Status.INSTALL -> {
                downloadButton.text = "安装"
            }
            Status.INSTALLED -> {
                downloadButton.text = "已安装"
                downloadButton.isEnabled = false
            }
            else -> {}
        }
    }
}