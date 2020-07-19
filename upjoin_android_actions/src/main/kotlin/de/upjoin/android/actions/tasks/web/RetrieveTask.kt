package de.upjoin.android.actions.tasks.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.upjoin.android.core.logging.Logger
import java.io.*
import java.net.URLConnection

abstract class RetrieveTask<R>(private val resultClass: Class<R>): AbstractAPITask<R>() {

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

    override suspend fun readResponse(urlConnection: URLConnection): R? {
        if (isHttpOk) {
            BufferedInputStream(urlConnection.inputStream).use { stream -> return readObject(stream) }
        }

        Logger.error(this, "GET Request on URL ${getURL()} returned error code $httpCode")
        return null
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