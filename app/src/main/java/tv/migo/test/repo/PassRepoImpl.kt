package tv.migo.test.repo

import tv.migo.test.db.dao.PassDao
import tv.migo.test.db.data.PassType
import tv.migo.test.db.entity.PassInfo
import tv.migo.test.utils.alignToEndOfTheDay
import java.util.*
import javax.inject.Inject

class PassRepoImpl @Inject constructor(private val passDao: PassDao) : PassRepository {
    private val calendar = Calendar.getInstance()
    override fun getPasses() = passDao.getPassesLiveData()

    override suspend fun addPass(passInfo: PassInfo) = passDao.addPasses(passInfo)

    override suspend fun activatePass(passInfo: PassInfo): Int {
        val now = System.currentTimeMillis()
        return passDao.activatePass(
            passType = passInfo.passType,
            passLength = passInfo.passLength,
            activeAt = now,
            expiredAt = getExpiredTime(passInfo, now)
        )
    }

    private fun getExpiredTime(passInfo: PassInfo, activeTime: Long): Long {
        calendar.timeInMillis = activeTime
        return when (passInfo.passType) {
            PassType.DAY -> {
                calendar.apply {
                    add(Calendar.DAY_OF_MONTH, passInfo.passLength) //add $passLength days.
                }.alignToEndOfTheDay().timeInMillis
            }
            PassType.HOUR -> {
                calendar.apply {
                    add(Calendar.HOUR, passInfo.passLength) //add $passLength hours.
                }.timeInMillis
            }
        }
    }
}