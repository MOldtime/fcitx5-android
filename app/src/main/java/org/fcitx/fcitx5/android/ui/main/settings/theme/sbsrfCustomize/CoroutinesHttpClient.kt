/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */

package org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize.data.RspReleaseData.RspReleaseData
import org.fcitx.fcitx5.android.ui.main.settings.theme.sbsrfCustomize.data.RspTmpDownloadData.RspTmpDownloadData
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL

class CoroutinesHttpClient {

    companion object {
//        const val BASE_URL = "http://192.168.1.5:8080"

        const val BASE_URL = "https://5b042a5455e0480fa806fc9483f9a8a1-cn-chengdu.alicloudapi.com"
    }

    suspend fun getReleases(pageToken: String?, pageSize: Int): RspReleaseData? {
        val url =
            "$BASE_URL/external/v1/releases?page_token=${pageToken ?: ""}&page_size=${pageSize}"
        val data = get(url)
        return data?.let {
            try {
                return Json.decodeFromString<RspReleaseData>(it)
            } catch (e: Exception) {
                Timber.e(e)
                null
            }
        }
    }

    suspend fun getTmpDownloadURL(fileToken: String): String? {
        val url = "$BASE_URL/external/v1/get_tmp_download_url?file_token=$fileToken"
        val data = get(url)
        return data?.let {
            try {
                val tmpData = Json.decodeFromString<RspTmpDownloadData>(it)
                if (tmpData.verifyCode()) {
                    tmpData.data.tmpDownloadUrl
                } else {
                    null
                }
            } catch (_: Exception) {
                null
            }
        }
    }

    suspend fun get(urlString: String): String? = withContext(Dispatchers.IO) {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.setRequestProperty("Content-Type", "application/json")

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().readText()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            connection.disconnect()
        }
    }

    suspend fun post(urlString: String, json: String): String? = withContext(Dispatchers.IO) {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.setRequestProperty("Content-Type", "application/json")

            connection.outputStream.use { output ->
                output.write(json.toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().readText()
            } else {
                Timber.d("respose: ${responseCode}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            connection.disconnect()
        }
    }
}