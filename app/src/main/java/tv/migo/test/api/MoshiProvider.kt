package tv.migo.test.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiProvider {
    private val moshiBuilder = Moshi.Builder().apply {
        addLast(KotlinJsonAdapterFactory())
    }
    val moshi = moshiBuilder.build()
}