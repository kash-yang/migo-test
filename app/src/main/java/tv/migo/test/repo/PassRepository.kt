package tv.migo.test.repo

import androidx.lifecycle.LiveData
import tv.migo.test.db.entity.PassInfo

interface PassRepository {
    fun getPasses(): LiveData<List<PassInfo>>
    suspend fun addPass(passInfo: PassInfo): Long
    suspend fun activatePass(passInfo: PassInfo): Int
}