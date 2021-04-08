package de.upjoin.android.actions.tasks.web

import de.upjoin.android.actions.actionModule
import de.upjoin.android.actions.tasks.AbstractTask
import de.upjoin.android.core.application.appInfoModule
import de.upjoin.android.core.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.*

/**
 * Abstract superclass for all http tasks
 */
abstract class AbstractHTTPTask<R>: AbstractTask<R>(), HTTPTask<R> {

    override var httpCode: Int? = null

    protected val isHttpOk: Boolean get() = httpCode == HttpURLConnection.HTTP_OK

    protected open val connectTimeout = 10000
    protected open val readTimeout = 20000

    @Throws(MalformedURLException::class)
    protected abstract fun getURL(): URL

    @Throws(IOException::class)
    protected open suspend fun openURLConnection(): HttpURLConnection = withContext(Dispatchers.IO) {

        webServiceInfoModule.initConnectionForDebug()

        val urlConnection = getURL().openConnection() as HttpURLConnection
        urlConnection.setRequestProperty("appversion", appInfoModule.appVersion)
        urlConnection.setRequestProperty("locale", Locale.getDefault().toString())
        urlConnection.connectTimeout = connectTimeout
        urlConnection.readTimeout = readTimeout

        return@withContext urlConnection
    }

    @Throws(IOException::class)
    protected abstract fun transformResponseBody(stream: InputStream): R

    @Throws(IOException::class)
    protected abstract fun transformResponseBody(string: String): R

    @Throws(IOException::class)
    protected open fun readObject(stream: InputStream): R {
        if (!taskType.debugTask(this)) {
            return transformResponseBody(stream) as R
        }

        val responseAsString = inputStreamToString(stream)
        actionModule.debugResponse(this, responseAsString)
        return transformResponseBody(responseAsString)
    }

    open suspend fun readResponse(urlConnection: URLConnection): R? {
        if (isHttpOk) {
            BufferedInputStream(urlConnection.inputStream).use {
                    stream -> return readObject(stream)
            }
        }
        else if (taskType.debugTask(this)){
            try {
                BufferedInputStream(urlConnection.inputStream).use { stream ->
                    val responseAsString = inputStreamToString(stream)
                    actionModule.debugResponse(this, responseAsString)
                }
            }
            catch (e: IOException) {
                try
                {
                    (urlConnection as HttpURLConnection).errorStream?.let { errorStream ->
                        BufferedInputStream(errorStream).use { stream ->
                            val responseAsString = inputStreamToString(stream)
                            actionModule.debugResponse(this, responseAsString)
                        }
                    }
                }
                catch (e: IOException) {
                    // do nothing when failing to read error stream
                }
            }
        }

        Logger.error(this, "Request on URL ${getURL()} returned error code $httpCode")
        return null
    }

    protected open suspend fun prepareRequest(urlConnection: URLConnection) {}

    override suspend fun runSecure(): R? =
        withContext(Dispatchers.IO) {
            val urlConnection = openURLConnection()
            try {
                prepareRequest(urlConnection)
                httpCode = urlConnection.responseCode
                return@withContext readResponse(urlConnection)
            } finally {
                urlConnection.disconnect()
            }
        }

    override fun handleException(e: Exception) {
        super.handleException(e)
        if (isHttpOk) { httpCode = null }
    }

    @Throws(IOException::class)
    protected fun inputStreamToString(inputStream: InputStream): String {
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
