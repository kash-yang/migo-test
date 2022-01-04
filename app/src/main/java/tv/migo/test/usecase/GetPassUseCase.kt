package tv.migo.test.usecase

import tv.migo.test.di.PassRepo
import tv.migo.test.repo.PassRepository
import javax.inject.Inject

class GetPassUseCase @Inject constructor(@PassRepo private val repository: PassRepository) {

    fun getPassesLiveData() = repository.getPasses()
}