package tv.migo.test.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

object ConnectivityUtils {
    enum class Type {
        BOTH,
        WIFI,
        MOBILE
    }

    fun hasAvailableNetwork(context: Context, type: Type = Type.BOTH): Boolean {
        return when(type) {
            Type.WIFI -> hasWiFiNetwork(context)
            Type.MOBILE -> hasMobileNetwork(context)
            else -> hasNetwork(context)
        }
    }

    private fun hasNetwork(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return when(val network = manager?.activeNetwork) {
            null -> false
            else -> {
                manager.getNetworkCapabilities(network)?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
                        manager.getNetworkCapabilities(network)?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
            }
        }
    }

    private fun hasWiFiNetwork(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        when(val activeNetwork = manager?.allNetworks) {
            null -> return false
            else -> {
                activeNetwork.forEach { network ->
                    if (isWiFiNetworkAvailable(manager, network)) {
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun hasMobileNetwork(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        when(val activeNetwork = manager?.allNetworks) {
            null -> return false
            else -> {
                activeNetwork.forEach { network ->
                    if (isMobileNetworkAvailable(manager, network)) {
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun isWiFiNetworkAvailable(manager: ConnectivityManager, network: Network): Boolean {
        return manager.getNetworkCapabilities(network)?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }

    private fun isMobileNetworkAvailable(manager: ConnectivityManager, network: Network): Boolean {
        return manager.getNetworkCapabilities(network)?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
    }
}