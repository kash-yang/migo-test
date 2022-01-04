package tv.migo.test.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tv.migo.test.api.services.MigoStatusService
import tv.migo.test.db.data.PassType
import tv.migo.test.db.entity.PassInfo
import tv.migo.test.usecase.ActivatePassUseCase
import tv.migo.test.usecase.AddPassUseCase
import tv.migo.test.usecase.GetPassUseCase
import tv.migo.test.usecase.GetStatusUseCase
import tv.migo.test.utils.Resource
import javax.inject.Inject

@HiltViewModel
class PassViewModel @Inject constructor(
    private val getPassUseCase: GetPassUseCase,
    private val addPassUseCase: AddPassUseCase,
    private val activatePassUseCase: ActivatePassUseCase,
    private val getStatusUseCase: GetStatusUseCase,
    ): ViewModel() {

    fun getStatus() = getStatusUseCase.getStatus()

    fun getPassesLiveData() = getPassUseCase.getPassesLiveData()

    fun addPass(passType: PassType, passLength: Int, price: Int): Flow<Resource<Unit>> {
        val passInfo = createPass(passType, passLength, price)
        return addPassUseCase.addPass(passInfo)
    }

    fun activatePass(passInfo: PassInfo) = activatePassUseCase.activatePass(passInfo)

    private fun createPass(passType: PassType, passLength: Int, price: Int) =
        PassInfo(
            passType = passType,
            passLength = passLength,
            price = price,
            generateAt = System.currentTimeMillis()
        )
}