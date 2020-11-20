package de.upjoin.android.view.application

import androidx.lifecycle.LiveData

enum class NetworkConnectionType {
    WIFI, CELLULAR, OTHER, NONE
}

class NetworkStateEvent(var connectedTo: NetworkConnectionType) {

    fun isOffline() = connectedTo == NetworkConnectionType.NONE
    fun isWifi() = connectedTo == NetworkConnectionType.WIFI
}

object NetworkState: LiveData<NetworkStateEvent>(NetworkStateEvent(NetworkConnectionType.NONE)) {

    // should be internal, but is public for testing
    fun notify(event: NetworkStateEvent) {
        postValue(event)
    }
}