package tv.migo.test

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.HiltAndroidApp
import tv.migo.test.api.ApiServices

@HiltAndroidApp
class MigoApplication: Application() {
    lateinit var apiServices: ApiServices
    var currentNetwork: Network? = null
        private set(value) {
            field = value
            currentNetworkCapabilities = when (value) {
                null -> null
                else -> connectivityManager.getNetworkCapabilities(value)
            }
        }

    var currentNetworkCapabilities: NetworkCapabilities? = null
        private set

    lateinit var connectivityManager: ConnectivityManager

    private val request = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            currentNetwork = network
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            currentNetwork = null
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            currentNetwork = null
        }

        override fun onUnavailable() {
            super.onUnavailable()
            currentNetwork = null
        }
    }

    override fun onCreate() {
        super.onCreate()
        apiServices = ApiServices(this)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }
}