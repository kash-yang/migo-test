package tv.migo.test.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tv.migo.test.db.convert.PassTypeConvert
import tv.migo.test.db.dao.PassDao
import tv.migo.test.db.entity.PassInfo


@Database(
    entities = [
        PassInfo::class
    ],
    version = 1
)
@TypeConverters(
    PassTypeConvert::class
)
abstract class MigoDatabase : RoomDatabase() {
    abstract fun passDao(): PassDao
    companion object {

        private const val TAG = "EcogenieDatabase"

        @Volatile
        private var mInstance: MigoDatabase? = null

        fun init(context: Context) = mInstance ?: synchronized(this) {
            mInstance ?: Room.databaseBuilder(
                context.applicationContext,
                MigoDatabase::class.java,
                "MigoDatabase.db")

                .build().also {
                    mInstance = it
                }
        }
    }
}