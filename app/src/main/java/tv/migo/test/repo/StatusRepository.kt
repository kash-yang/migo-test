package tv.migo.test.repo

import retrofit2.Response
import tv.migo.test.api.model.MigoStatus

interface StatusRepository {
    suspend fun getStatus(): Response<MigoStatus>
}