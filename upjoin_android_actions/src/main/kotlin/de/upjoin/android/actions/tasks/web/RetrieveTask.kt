package de.upjoin.android.actions.tasks.web

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection

abstract class RetrieveTask<R>(private val resultClass: Class<R>): AbstractHTTPTask<R>() {

    private val objectMapper: ObjectMapper by lazy { taskType.createObjectMapper() }

    protected open val contentType = "application/json"

    override suspend fun openURLConnection(): HttpURLConnection {
        val urlConnection = super.openURLConnection()
        urlConnection.setRequestProperty("Content-Type", contentType)
        urlConnection.setRequestProperty("charset", "utf-8")
        return urlConnection
    }

    @Throws(IOException::class)
    override fun transformResponseBody(stream: InputStream): R {
        if (resultClass == String::class.java) {
            return inputStreamToString(stream) as R
        }
        InputStreamReader(stream).use { r ->
            return objectMapper.readValue(r, resultClass)
        }
    }

    @Throws(IOException::class)
    override fun transformResponseBody(string: String): R {
        return objectMapper.readValue(string, resultClass)
    }

}
