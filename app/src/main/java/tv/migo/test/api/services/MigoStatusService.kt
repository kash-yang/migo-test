package tv.migo.test.api.services

import retrofit2.Response
import retrofit2.http.GET
import tv.migo.test.api.model.MigoStatus

interface MigoStatusService {

    @GET("status")
    suspend fun getStatus(): Response<MigoStatus>
}