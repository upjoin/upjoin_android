package de.upjoin.android.actions.tasks.web

import de.upjoin.android.actions.tasks.AbstractTask
import de.upjoin.android.core.application.appInfoModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.*

abstract class AbstractAPITask<R>: AbstractTask<R>(), APITask<R> {

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

    protected abstract suspend fun readResponse(urlConnection: URLConnection): R?

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

}