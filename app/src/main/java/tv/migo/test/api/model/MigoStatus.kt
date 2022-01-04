package tv.migo.test.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MigoStatus(
    val status: Int,
    val message: String
)
