package tv.migo.test.api.model

import com.squareup.moshi.JsonClass

const val EXCEPTION_ERROR = 900
const val EXCEPTION_SOCKET_TIMEOUT_ERROR = 901
const val EXCEPTION_SOCKET_ERROR = 902
const val EXCEPTION_IO_ERROR = 903

@JsonClass(generateAdapter = true)
data class MigoError(
    val code: Int = EXCEPTION_ERROR,
    val message: String = "server not response error message."
)