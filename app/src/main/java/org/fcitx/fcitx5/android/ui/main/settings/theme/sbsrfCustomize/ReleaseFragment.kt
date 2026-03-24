/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.fcitx.fcitx5.android.utils.toast

class ReleaseFragment : Fragment() {
    private lateinit var adapter: ReleasePagingAdapter
    private lateinit var viewModel: ViewModel
    private lateinit var recyclerView: RecyclerView
    private var isBound = false
    private var binder: DownloadAndInstallService.DownloadBinder? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?, service: IBinder?
        ) {
            binder = service as DownloadAndInstallService.DownloadBinder
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            binder = null
        }
    }

    private fun bindService() {
        if (!isBound) {
            context?.let { ctx ->
                DownloadAndInstallService.createIntent().also { intent ->
                    ctx.bindService(intent, connection, Context.BIND_AUTO_CREATE)
                }
            }
        }
    }

    private fun unbindService() {
        if (isBound) {
            context?.unbindService(connection)
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        bindService()
    }

    override fun onStop() {
        super.onStop()
        unbindService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        recyclerView = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            layoutManager = LinearLayoutManager(context)
        }
        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ReleasePagingAdapter().apply {
            setAdapter()
        }
        viewModel = ViewModel()
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = null
        observePagingData()
    }

    fun showLoadingDialog(): AlertDialog {
        val progressBar = ProgressBar(context)

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            addView(progressBar)
        }

        return AlertDialog.Builder(context).setView(container).setCancelable(false).create()
            .apply { show() }
    }

    private fun ReleasePagingAdapter.setAdapter() {
        onClick = { data, position ->
            startServiceAction()?.let { binder ->
                when (status) {
                    is DownloadState.Idle -> {
                        val globalState = DownloadAndInstallService.statusEvent.value
                        if (globalState is DownloadState.Downloading) {
                            context.toast("已有文件在下载, 请点击暂停")
                            return@let
                        }
                        if (globalState is DownloadState.Downloaded) {
                            AlertDialog.Builder(context)
                                .setMessage("已有文件等待安装，这将会清除下载的文件并重新下载")
                                .setTitle("是否继续").setPositiveButton("继续下载") { _, _ ->
                                    binder.reset()
                                    lifecycleScope.launch {
                                        val loadingDialog = showLoadingDialog()
                                        val url =
                                            viewModel.httpClient.getTmpDownloadURL(data.fileToken)
                                        loadingDialog.dismiss()
                                        url?.let {
                                            binder.downloadFile(
                                                data.fileToken, data.fileName, it
                                            )
                                        } ?: {
                                            context.toast("获取链接失败")
                                        }
                                    }
                                }.setNeutralButton("取消") { _, _ -> }.create().show()
                        } else {
                            AlertDialog.Builder(context).setMessage("下载 ${data.fileVersion}")
                                .setTitle("是否下载").setPositiveButton("下载") { _, _ ->
                                    lifecycleScope.launch {
                                        if (url == null) {
                                            val loadingDialog = showLoadingDialog()
                                            url =
                                                viewModel.httpClient.getTmpDownloadURL(data.fileToken)
                                            loadingDialog.dismiss()
                                        }
                                        url?.let {
                                            binder.downloadFile(
                                                data.fileToken, data.fileName, it
                                            )
                                        } ?: {
                                            context.toast("获取下载链接失败")
                                        }
                                    }
                                }.setNeutralButton("取消") { _, _ -> }.create().show()
                        }
                    }
                    is DownloadState.Downloading -> {
                        AlertDialog.Builder(context).setMessage("这将删除已下载的文件")
                            .setTitle("是否取消下载").setPositiveButton("确认") { _, _ ->
                                binder.cancelDownload()
                            }.setNeutralButton("取消") { _, _ -> }.create().show()
                    }
                    is DownloadState.Downloaded -> {
                        AlertDialog.Builder(context).setMessage("这将会覆盖你的现有配置")
                            .setTitle("是否安装").setPositiveButton("安装") { _, _ ->
                                lifecycleScope.launch {
                                    val loadingDialog = showLoadingDialog()
                                    if (binder.installFile()) {
                                        context.toast("安装成功")
                                    } else {
                                        context.toast("安装失败")
                                    }
                                    loadingDialog.dismiss()
                                }
                            }.setNeutralButton("取消") { _, _ -> }.create().show()
                    }
                    else -> {}
                }
            }
        }

        onLongClick = {
            startServiceAction()?.let { binder ->
                if (DownloadAndInstallService.statusEvent.value is DownloadState.Downloaded) {
                    AlertDialog.Builder(context).setMessage("删除已下载的文件").setTitle("是否删除")
                        .setPositiveButton("删除") { _, _ ->
                            binder.reset()
                        }.setNeutralButton("取消") { _, _ -> }.create().show()
                }
            }
        }
    }

    private fun observePagingData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getReleases()
                    .collectLatest { pagingData ->
                        adapter.submitData(pagingData)
                    }
            }
        }
    }

    private fun startServiceAction(): DownloadAndInstallService.DownloadBinder? {
        if (!isBound || binder == null) {
            context?.let {
                DownloadAndInstallService.start()
            }
            bindService()
        }
        return binder
    }
}