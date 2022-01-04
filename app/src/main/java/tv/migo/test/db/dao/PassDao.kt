package tv.migo.test.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tv.migo.test.db.data.PassType
import tv.migo.test.db.entity.PassInfo

@Dao
interface PassDao {

    @Query("SELECT * FROM pass ORDER BY pass_type ASC, pass_length ASC")
    suspend fun getPasses(): List<PassInfo>

    @Query("SELECT * FROM pass ORDER BY pass_type ASC, pass_length ASC")
    fun getPassesLiveData(): LiveData<List<PassInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPasses(passInfo: PassInfo): Long

    @Query("UPDATE pass SET active_at = :activeAt, expired_at = :expiredAt WHERE pass_type = :passType AND pass_length = :passLength")
    suspend fun activatePass(passType: PassType, passLength: Int, activeAt: Long, expiredAt: Long): Int
}