package tv.migo.test.api.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import tv.migo.test.BuildConfig
import tv.migo.test.MigoApplication

class DynamicBaseURLInterceptor(private val application: MigoApplication) : Interceptor {
    private val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    override fun intercept(chain: Interceptor.Chain): Response {
        val network = application.currentNetwork
        return when (application.currentNetworkCapabilities?.hasTransport(TRANSPORT_WIFI)) {
            true -> {
                connectivityManager.bindProcessToNetwork(network)
                chain.proceed(changeUrl(chain.request()))
            }
            else -> {
                connectivityManager.bindProcessToNetwork(network)
                chain.proceed(chain.request())
            }
        }
    }

    private fun changeUrl(request: Request) : Request {
        val builder = request.newBuilder()
        val originalUri = request.url.toUri()
        builder.url(originalUri.toString()
            .replace(BuildConfig.MIGO_PUBLIC_API_SERVER, BuildConfig.MIGO_PRIVATE_API_SERVER))
        return builder.build()
    }
}