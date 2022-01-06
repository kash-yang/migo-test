package tv.migo.test.api

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import tv.migo.test.api.mock.MockMigoStatusServiceHandler

class MockInterceptor : Interceptor {

    private val serviceHandlerList = listOf(
        MockMigoStatusServiceHandler(),
    )
    override fun intercept(chain: Interceptor.Chain): Response {
        val uri = chain.request().url.toUri().toString()
        println("intercept uri: $uri")
        val jsonType = "application/json".toMediaTypeOrNull()

        serviceHandlerList.first { it.interrupt(uri) }.let {
            it.handleUri(uri) ?: ""
        }.also {
            println("resp from serviceHandlerList: $uri")
            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(it)
                .body(it.toByteArray().toResponseBody(jsonType))
                .addHeader("content-type", "application/json")
                .build()
        }
    }
}