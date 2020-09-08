package de.upjoin.android.actions.tasks.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.upjoin.android.core.application.appInfoModule
import de.upjoin.android.core.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URLConnection

abstract class PostTask<U, R>(private val resultClass: Class<R>): AbstractHTTPTask<R>() {

    private val objectMapper = jacksonObjectMapper()

    @Throws(IOException::class)
    protected fun readObject(stream: InputStream): R {
        if (resultClass == String::class.java) {
            return inputStreamToString(stream) as R
        }
        InputStreamReader(stream).use { r ->
            return objectMapper.readValue(r, resultClass)
        }
    }

    protected abstract fun fetchRequest(): U?

    override suspend fun prepareRequest(urlConnection: URLConnection) {
        val request = fetchRequest()

        //byte[] postDataBytes = new Gson().toJson(request).toString().getBytes("UTF-8");
        urlConnection.doOutput = true
        (urlConnection as HttpURLConnection).instanceFollowRedirects = false
        urlConnection.setRequestProperty("Content-Type", "application/json")
        urlConnection.setRequestProperty("charset", "utf-8")
        //urlConnection.setRequestProperty( "Content-Length", String.valueOf(postDataBytes.length));
        urlConnection.useCaches = false

        // write request
        DataOutputStream(urlConnection.outputStream).use { wr ->
            // some POST requests don't even need a request body
            if (request != null) {
                if (appInfoModule.isDebug) {
                    Logger.debug(this, objectMapper.writeValueAsString(request))
                }
                objectMapper.writeValue(wr as OutputStream, request)
            }
        }
    }

    override suspend fun readResponse(urlConnection: URLConnection): R? {
        if (isHttpOk) {
            BufferedInputStream(urlConnection.inputStream).use {
                    stream -> return readObject(stream)
            }
        }

        Logger.error(this, "Post Request on URL ${getURL()} returned error code ${httpCode}")
        return null
    }

    override suspend fun openURLConnection() = withContext(Dispatchers.IO) {
        val urlConnection = super.openURLConnection()
        urlConnection.requestMethod = "POST"
        return@withContext urlConnection
    }

    @Throws(IOException::class)
    private fun inputStreamToString(inputStream: InputStream): String {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int = inputStream.read(buffer)
        while (length > 0) {
            result.write(buffer, 0, length)
            length = inputStream.read(buffer)
        }
        // StandardCharsets.UTF_8.name() > JDK 7
        return result.toString("UTF-8")
    }

}