package tv.migo.test.api

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import tv.migo.test.MigoApplication
import tv.migo.test.api.model.*
import tv.migo.test.api.services.MigoStatusService
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton


inline fun <T> executeApi(hemsApi: () -> Response<T>): Response<T> {
    return try {
        hemsApi()
    } catch (e: Exception) {
        val code = when(e) {
            is SocketTimeoutException -> EXCEPTION_SOCKET_TIMEOUT_ERROR
            is SocketException -> EXCEPTION_SOCKET_ERROR
            is IOException -> EXCEPTION_IO_ERROR
            else -> EXCEPTION_ERROR
        }

        val error = MoshiProvider.moshi.adapter(MigoError::class.java).toJson(
            if(e.message != null) {
                MigoError(code, e.message!!)
            } else {
                MigoError(code)
            })
        val body = error.toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return Response.error(code, body)
    }
}

@Singleton
class ApiServices @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val retrofitServiceGenerator = RetrofitServiceGenerator(context.applicationContext as MigoApplication)
    val migoStatusService: MigoStatusService by lazy { retrofitServiceGenerator.createService(MigoStatusService::class.java) }
}