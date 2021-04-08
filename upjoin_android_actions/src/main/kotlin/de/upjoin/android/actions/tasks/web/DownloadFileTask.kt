package de.upjoin.android.actions.tasks.web

import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URLConnection

abstract class DownloadFileTask(private val streamHandler: suspend (stream: InputStream) -> Unit) :
    AbstractHTTPTask<Boolean>() {

    override suspend fun readResponse(urlConnection: URLConnection): Boolean? {
        if (isHttpOk) {
            BufferedInputStream(urlConnection.inputStream).use { stream -> streamHandler(stream) }
            return true
        }
        return null
    }

    final override fun transformResponseBody(stream: InputStream) = true
    final override fun transformResponseBody(string: String) = true

}
