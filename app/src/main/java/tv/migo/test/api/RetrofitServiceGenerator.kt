package tv.migo.test.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tv.migo.test.BuildConfig
import tv.migo.test.MigoApplication
import tv.migo.test.api.interceptor.DynamicBaseURLInterceptor
import java.util.concurrent.TimeUnit

class RetrofitServiceGenerator(private val migoApplication: MigoApplication) {
    private val moshiConverterFactory = MoshiConverterFactory
        .create(MoshiProvider.moshi)

    private val loggingIntercept = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        private val TAG = "APIService"
        private val LIMIT_CHARS = 400
        override fun log(message: String) {
            if (message.length > LIMIT_CHARS) {
                var temp = message
                while (temp.isNotEmpty()) {
                    temp = if (temp.length > LIMIT_CHARS) {
                        Log.i(TAG, temp.substring(0, LIMIT_CHARS))
                        temp.substring(LIMIT_CHARS, temp.length)
                    } else {
                        Log.i(TAG, temp)
                        ""
                    }
                }
            } else {
                Log.i(TAG, message)
            }
        }
    }).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder().apply {
        cache(null)
        followRedirects(false)
        followSslRedirects(false)
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        addInterceptor(DynamicBaseURLInterceptor(migoApplication))
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingIntercept)
        }
    }

    private val retrofitBuilder = Retrofit.Builder().apply {
        baseUrl(BuildConfig.MIGO_PUBLIC_API_SERVER)
        addConverterFactory(moshiConverterFactory)
        client(httpClient.build())
    }

    val retrofit: Retrofit = retrofitBuilder.build()

    fun <SERVICE> createService(serviceClass: Class<SERVICE>): SERVICE {
        return retrofit.create(serviceClass)
    }
}