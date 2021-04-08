package de.upjoin.android.actions.tasks.web

import com.fasterxml.jackson.databind.ObjectMapper
import de.upjoin.android.actions.actionModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URLConnection

abstract class PostTask<U, R>(private val resultClass: Class<R>): AbstractHTTPTask<R>() {

    private val objectMapper: ObjectMapper by lazy { taskType.createObjectMapper() }

    protected abstract fun fetchRequest(): U?

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
                if (taskType.debugTask(this)) {
                    val requestString = objectMapper.writeValueAsString(request)
                    actionModule.debugRequest(this, requestString)
                }
                objectMapper.writeValue(wr as OutputStream, request)
            }
        }
    }

    override suspend fun openURLConnection() = withContext(Dispatchers.IO) {
        val urlConnection = super.openURLConnection()
        urlConnection.requestMethod = "POST"
        return@withContext urlConnection
    }

}
