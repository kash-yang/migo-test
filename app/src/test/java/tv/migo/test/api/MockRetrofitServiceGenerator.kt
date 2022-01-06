package tv.migo.test.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tv.migo.test.BuildConfig
import tv.migo.test.api.interceptor.DynamicBaseURLInterceptor

class MockRetrofitServiceGenerator(private val interceptor: DynamicBaseURLInterceptor? = null) {

    private val httpClient = OkHttpClient.Builder().apply {
        cache(null)
        followRedirects(false)
        followSslRedirects(false)
        interceptor?.let {
            addInterceptor(it)
        }
//        addInterceptor(MockInterceptor())
    }

    private val retrofitBuilder = Retrofit.Builder().apply {
        baseUrl(BuildConfig.MIGO_PUBLIC_API_SERVER)
        addConverterFactory(MoshiConverterFactory.create(MoshiProvider.moshi))
        client(httpClient.build())
    }

    private val retrofit: Retrofit = retrofitBuilder.build()

    fun <SERVICE> createService(serviceClass: Class<SERVICE>): SERVICE {
        return retrofit.create(serviceClass)
    }
}