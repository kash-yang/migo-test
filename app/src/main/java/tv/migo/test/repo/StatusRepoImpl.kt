package tv.migo.test.repo

import tv.migo.test.api.services.MigoStatusService
import javax.inject.Inject

class StatusRepoImpl @Inject constructor(private val migoStatusService: MigoStatusService) : StatusRepository {

    override suspend fun getStatus() = migoStatusService.getStatus()
}