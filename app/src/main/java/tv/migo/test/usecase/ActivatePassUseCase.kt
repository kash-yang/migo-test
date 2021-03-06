package tv.migo.test.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import tv.migo.test.db.entity.PassInfo
import tv.migo.test.di.PassRepo
import tv.migo.test.repo.PassRepository
import tv.migo.test.utils.Resource
import javax.inject.Inject

class ActivatePassUseCase @Inject constructor(@PassRepo private val repository: PassRepository) {

    fun activatePass(passInfo: PassInfo) = flow<Resource<Unit>> {
        val result = repository.activatePass(passInfo)
        if(result <= 0) {
            emit(Resource.error(Exception("activate failed")))
        } else {
            emit(Resource.success(null))
        }
    }.onStart { emit(Resource.loading(null)) }.flowOn(Dispatchers.IO)
}