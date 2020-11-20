package de.upjoin.android.view.modules

interface NetworkCallbackModule {

    companion object {
        const val MODULE_ID = "NetworkCallbackModule"
    }

    fun registerNetworkCallback()
    fun unregisterNetworkCallback()
}

lateinit var networkCallbackModule: NetworkCallbackModule