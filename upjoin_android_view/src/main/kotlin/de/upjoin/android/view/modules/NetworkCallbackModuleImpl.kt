package de.upjoin.android.view.modules

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import de.upjoin.android.core.application.ModulizedApplication
import de.upjoin.android.core.modules.ModuleLiveCycle
import de.upjoin.android.view.application.NetworkCallback

class NetworkCallbackModuleImpl: ModuleLiveCycle, NetworkCallbackModule {

    private lateinit var applicationContext: Context
    private lateinit var networkCallback: NetworkCallback

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    override fun onCreate(application: ModulizedApplication) {
        networkCallbackModule = this
        applicationContext = application.applicationContext
        networkCallback = NetworkCallback(applicationContext)
        registerNetworkCallback()
    }

    override fun onTerminate(application: ModulizedApplication) {
        unregisterNetworkCallback()
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    override fun registerNetworkCallback() {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
    }

    override fun unregisterNetworkCallback() {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}