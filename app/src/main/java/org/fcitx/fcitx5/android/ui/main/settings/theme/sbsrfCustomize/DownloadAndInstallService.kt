/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.fcitx.fcitx5.android.utils.appContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class DownloadAndInstallService : Service() {
    companion object {
        private const val DIR = "DownloadFile"
        private const val CHANNEL_ID = "download_install_channel"
        private const val NOTIFICATION_ID = 1001
        private var downloadFileJob: Deferred<Boolean>? = null
        private var file: File? = null
        private val _statusEvent = MutableStateFlow<DownloadState>(DownloadState.Idle)
        val statusEvent = _statusEvent.asStateFlow()

        var fileToken: String? = null
            private set

        // 启动服务的便捷方法
        fun start() {
            val intent = createIntent()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                appContext.startForegroundService(intent)
            } else {
                appContext.startService(intent)
            }
        }

        fun createIntent(): Intent {
            return Intent(appContext, DownloadAndInstallService::class.java).apply {
                action = "START_DOWNLOAD"
            }
        }
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private suspend fun emit(value: DownloadState) {
        when (value) {
            is DownloadState.Idle -> {
                fileToken = null
            }
            else -> {}
        }
        _statusEvent.emit(value)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).build()
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            } else {
                0
            },
        )
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        return DownloadBinder()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "下载服务", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onTimeout(startId: Int) {
        super.onTimeout(startId)
        stopSelf()
    }

    inner class DownloadBinder : Binder() {

        private fun download(
            fileName: String,
            urlString: String,
        ): Deferred<Boolean> = serviceScope.async(Dispatchers.IO) {
            val appDir = File(getExternalFilesDir(null), DIR)

            if (!appDir.exists()) {
                appDir.mkdirs()
            }

            file = File(appDir, fileName)

            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 15000
                connection.readTimeout = 15000

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val fileSize = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        connection.contentLengthLong
                    } else {
                        connection.contentLength.toLong()
                    }
                    var currentDownload = 0L

                    val job = launch {
                        while (fileSize > currentDownload) {
                            emit(DownloadState.Downloading(((currentDownload.toDouble() / fileSize.toDouble()) * 100).toInt()))
                            delay(300)
                        }
                    }

                    connection.inputStream.use { input ->
                        FileOutputStream(file).use { output ->
                            val buffer = ByteArray(4096)
                            var bytesRead: Int
                            while (input.read(buffer).also { bytesRead = it } != -1) {
                                output.write(buffer, 0, bytesRead)
                                yield()
                                currentDownload += bytesRead
                            }
                        }
                    }

                    connection.disconnect()
                    job.cancel()
                    emit(DownloadState.Downloaded)
                    return@async true
                } else {
                    connection.disconnect()
                    emit(DownloadState.Idle)
                    return@async false
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    file?.delete()
                    emit(DownloadState.Idle)
                    throw e
                }
                e.printStackTrace()
                return@async false
            }
        }

        suspend fun downloadFile(
            newFileToken: String,
            fileName: String,
            urlString: String,
        ): Boolean = withContext(Dispatchers.IO) {
            downloadFileJob = if (newFileToken == fileToken) {
                when (statusEvent.value) {
                    is DownloadState.Idle -> {
                        download(fileName, urlString)
                    }
                    is DownloadState.Downloading -> {
                        downloadFileJob
                    }
                    is DownloadState.Downloaded -> {
                        return@withContext true
                    }
                    is DownloadState.Installed -> {
                        return@withContext false
                    }
                }
            } else {
                fileToken = newFileToken
                downloadFileJob?.cancelAndJoin()
                download(fileName, urlString)
            }
            val result = downloadFileJob!!.await()
            downloadFileJob = null
            return@withContext result
        }

        suspend fun installFile() = withContext(Dispatchers.IO) {
            val extractDir = File(
                File(
                    getExternalFilesDir(null), "data"
                ), "rime"
            )

            if (!extractDir.exists()) {
                extractDir.mkdirs()
            }

            return@withContext (try {
                ZipInputStream(FileInputStream(file)).use { zipInputStream ->
                    var entry: ZipEntry? = zipInputStream.nextEntry
                    while (entry != null) {
                        val entryFile = File(extractDir, entry.name)

                        if (entry.isDirectory) {
                            entryFile.mkdirs()
                        } else {
                            FileOutputStream(entryFile).use { output ->
                                zipInputStream.copyTo(output)
                            }
                        }

                        zipInputStream.closeEntry()
                        entry = zipInputStream.nextEntry
                    }
                }

                file?.delete()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }).also {
                if (it) {
                    fileToken?.let {
                        emit(DownloadState.Installed(it))
                    }
                }
            }
        }

        fun cancelDownload() {
            if (statusEvent.value is DownloadState.Downloading) {
                serviceScope.launch {
                    downloadFileJob?.cancelAndJoin().let {
                        downloadFileJob = null
                    }
                    reset()
                }
            }
        }

        fun reset() {
            file?.delete().apply {
                file = null
            }
            serviceScope.launch {
                emit(DownloadState.Idle)
            }
        }
    }
}