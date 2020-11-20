package de.upjoin.android.view.application

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import de.upjoin.android.core.framework.saveBlock

class NetworkCallback(private val applicationContext: Context) : ConnectivityManager.NetworkCallback() {

    private val connectedWifiNetworks = mutableSetOf<Long>()
    private val connectedCellularNetworks = mutableSetOf<Long>()
    private val connectedOtherNetworks = mutableSetOf<Long>()

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    override fun onAvailable(network: Network) {
        saveBlock {
            val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return

            val isWifi = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            val isCellular = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

            val notify: Boolean
            when {
                isWifi -> {
                    notify = connectedWifiNetworks.isEmpty()
                    connectedWifiNetworks.add(network.networkHandle)
                }
                isCellular -> {
                    notify = connectedCellularNetworks.isEmpty()
                    connectedCellularNetworks.add(network.networkHandle)
                }
                else -> {
                    notify = connectedOtherNetworks.isEmpty()
                    connectedOtherNetworks.add(network.networkHandle)
                }
            }
            if (notify) notifyChange()
        }
    }

    private fun notifyChange() {
        NetworkState.notify(
            NetworkStateEvent(
                when {
                    connectedWifiNetworks.isNotEmpty() -> NetworkConnectionType.WIFI
                    connectedCellularNetworks.isNotEmpty() -> NetworkConnectionType.CELLULAR
                    connectedOtherNetworks.isNotEmpty() -> NetworkConnectionType.OTHER
                    else -> NetworkConnectionType.NONE
                }
            )
        )
    }

    override fun onLost(network: Network) {
        saveBlock {
            val wasCellularEmpty = connectedCellularNetworks.isNotEmpty()
            val wasWifisEmpty = connectedWifiNetworks.isNotEmpty()
            val wasOthersEmpty = connectedOtherNetworks.isNotEmpty()

            connectedCellularNetworks.remove(network.networkHandle)
            connectedWifiNetworks.remove(network.networkHandle)
            connectedOtherNetworks.remove(network.networkHandle)

            if ((!wasCellularEmpty && connectedCellularNetworks.isEmpty()) ||
                (!wasWifisEmpty && connectedWifiNetworks.isEmpty()) ||
                (!wasOthersEmpty && connectedOtherNetworks.isEmpty())
            ) notifyChange()
        }
    }
}
