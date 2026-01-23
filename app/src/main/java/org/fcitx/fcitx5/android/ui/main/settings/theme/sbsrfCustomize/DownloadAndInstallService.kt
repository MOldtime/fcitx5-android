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
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.fcitx.fcitx5.android.data.prefs.AppPrefs
import org.fcitx.fcitx5.android.utils.appContext
import timber.log.Timber
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
        private var position: Int? = null
        private val sbxlmVersion = AppPrefs.getInstance().sbxlmVersion

        var progressJob: Job? = null
        private var status = sbxlmVersion.let {
            val value = it.getValue()
            if (value.isEmpty()) {
                StatusData(Status.IDLE, null, null)
            } else {
                StatusData(Status.INSTALLED, value, null)
            }
        }

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

    private var notifyItemStatusChanged: ((Int, Status) -> Unit)? = null
    private var notifyItemProgressChanged: ((Int, Int) -> Unit)? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private fun setStatus(value: StatusData) {
        if (value.status == Status.INSTALLED) {
            sbxlmVersion.setValue(status.fileToken ?: "")
        }
        if (status.status != value.status) {
            notifyItemStatusChanged(value.status)
        }
        status = value
    }

    private fun notifyItemStatusChanged(status: Status) {
        position?.let {
            serviceScope.launch {
                withContext(Dispatchers.Main) {
                    notifyItemStatusChanged?.invoke(it, status)
                }
            }
        }
    }

    private fun notifyItemProgressChanged(progress: Int) {
        position?.let {
            serviceScope.launch {
                withContext(Dispatchers.Main) {
                    notifyItemProgressChanged?.invoke(it, progress)
                }
            }
        }
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

    override fun onDestroy() {
        Timber.d("Service: onDestroy")
        super.onDestroy()
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

    data class StatusData(
        val status: Status,
        val fileToken: String? = null,
        val fileSize: Long? = null,
        var currentDownload: Long? = null
    )

    inner class DownloadBinder : Binder() {

        private suspend fun progress() {
            val flow = flow {
                while (true) {
                    emit(
                        (((status.currentDownload ?: 0).toDouble() / (status.fileSize
                            ?: 0).toDouble()) * 100).toInt()
                    )
                    delay(300)
                }
            }
            flow.collect {
                notifyItemProgressChanged(it)
            }
        }

        private fun downloadTest(
            fileToken: String,
            fileName: String,
            urlString: String,
        ): Deferred<Boolean> = serviceScope.async() {
            try {
                setStatus(
                    StatusData(
                        Status.DOWNLOADING, fileToken, 100, 0
                    )
                )
                progressJob = launch { progress() }
                for (i in 0..100) {
                    status.currentDownload = i.toLong()
                    delay(100)
                }
                progressJob?.cancel()
                setStatus(status.copy(status = Status.INSTALL))
                return@async true
            } catch (e: Exception) {
                if (e is CancellationException) {
                    progressJob?.cancel()
                    setStatus(StatusData(Status.IDLE))
                    throw e
                }
                e.printStackTrace()
                false
            }
        }

        private fun download(
            fileToken: String,
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
                    setStatus(
                        StatusData(
                            Status.DOWNLOADING, fileToken,
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                connection.contentLengthLong
                            } else {
                                connection.contentLength.toLong()
                            }, 0
                        )
                    )

                    progressJob = launch { progress() }

                    connection.inputStream.use { input ->
                        FileOutputStream(file).use { output ->
                            val buffer = ByteArray(4096)
                            var bytesRead: Int
                            while (input.read(buffer).also { bytesRead = it } != -1) {
                                output.write(buffer, 0, bytesRead)
                                yield()
                                status.currentDownload = status.currentDownload?.plus(bytesRead)
                            }
                        }
                    }

                    connection.disconnect()
                    setStatus(status.copy(status = Status.INSTALL))
                    return@async true
                } else {
                    connection.disconnect()
                    setStatus(StatusData(Status.IDLE))
                    return@async false
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    file?.delete()
                    setStatus(StatusData(Status.IDLE))
                    throw e
                }
                e.printStackTrace()
                return@async false
            } finally {
                progressJob?.cancel()
            }
        }

        suspend fun downloadFile(
            fileToken: String,
            fileName: String,
            urlString: String,
            position: Int,
        ): Boolean = withContext(Dispatchers.IO) {
            setPosition(position)
            downloadFileJob = if (fileToken == status.fileToken) {
                when (status.status) {
                    Status.DOWNLOADING -> {
                        progressJob?.cancel()
                        progressJob = launch { progress() }
                        downloadFileJob
                    }
                    Status.INSTALL -> {
                        return@withContext true
                    }
                    Status.IDLE -> {
                        download(fileToken, fileName, urlString)
                    }
                    Status.INSTALLED -> {
                        return@withContext false
                    }
                }
            } else {
                downloadFileJob?.cancelAndJoin()
                download(fileToken, fileName, urlString)
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
                    setStatus(status.copy(status = Status.INSTALLED))
                }
            }
        }

        fun getStatus(): Status {
            return status.status
        }

        fun getFileToken(): String? {
            return status.fileToken
        }

        fun cancelDownload() {
            if (status.status == Status.DOWNLOADING) {
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
            setStatus(StatusData(Status.IDLE))
        }

        fun setPosition(newPosition: Int) {
            position = newPosition
        }

        fun setNotifyItemStatusChanged(value: (Int, Status) -> Unit) {
            notifyItemStatusChanged = value
        }

        fun setNotifyItemProgressChanged(value: (Int, Int) -> Unit) {
            notifyItemProgressChanged = value
        }
    }
}