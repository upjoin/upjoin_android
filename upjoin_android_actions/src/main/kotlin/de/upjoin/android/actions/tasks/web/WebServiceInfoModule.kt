package de.upjoin.android.actions.tasks.web

import de.upjoin.android.core.application.appInfoModule
import de.upjoin.android.core.logging.Logger
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface WebServiceInfoModule {

    fun getWebServiceBaseUrl() = if (appInfoModule.isDebug) getDebugWebServiceBaseUrl() else getProdWebServiceBaseUrl()

    fun getDebugWebServiceBaseUrl(): String
    fun getProdWebServiceBaseUrl(): String

    fun initConnectionForDebug() {
        if (!appInfoModule.isDebug) return

        try {
            val tm = object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers() = null
            }
            //ProviderInstaller.installIfNeeded(getApplicationContext());

            val context = SSLContext.getInstance("SSL")
            context.init(null, arrayOf<TrustManager>(tm), null)
            HttpsURLConnection.setDefaultSSLSocketFactory(context.socketFactory)

            val nullVerifier = HostnameVerifier { hostname, session -> true }

            HttpsURLConnection.setDefaultHostnameVerifier(nullVerifier)
        } catch (e: NoSuchAlgorithmException) {
            Logger.error(this, e.message, e)
        } catch (e: KeyManagementException) {
            Logger.error(this, e.message, e)
        }
    }

    companion object {
        const val MODULE_ID = "WebServiceInfoModule"

        lateinit var instance: WebServiceInfoModule
    }
}

val webServiceInfoModule: WebServiceInfoModule by lazy { WebServiceInfoModule.instance }