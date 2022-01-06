package tv.migo.test.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import tv.migo.test.api.executeApi
import tv.migo.test.api.model.MigoStatus
import tv.migo.test.di.StatusRepo
import tv.migo.test.repo.StatusRepository
import tv.migo.test.utils.Resource
import javax.inject.Inject

class GetStatusUseCase @Inject constructor(@StatusRepo private val repository: StatusRepository) {
    fun getStatus() = flow<Resource<MigoStatus>> {
        val result = executeApi { repository.getStatus() }
        if(result.isSuccessful && result.body() != null) {
            emit(Resource.success(result.body()!!))
        } else {
            emit(Resource.error(Exception(result.message())))
        }
    }.flowOn(Dispatchers.IO)
}