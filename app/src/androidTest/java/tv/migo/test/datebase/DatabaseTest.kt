package tv.migo.test.datebase

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tv.migo.test.db.MigoDatabase
import tv.migo.test.db.dao.PassDao
import tv.migo.test.db.data.PassType
import tv.migo.test.db.entity.PassInfo
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var passDao: PassDao
    private lateinit var db: MigoDatabase

    private val pass: PassInfo = PassInfo(
        passType = PassType.DAY,
        passLength = 1,
        price = 500,
        generateAt = System.currentTimeMillis()
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MigoDatabase::class.java).build()
        passDao = db.passDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writePassAndReadInList() {
        runBlocking {
            passDao.addPasses(pass)
            val byPass = passDao.getPasses()
            assertThat(byPass[0].passType, equalTo(pass.passType))
            assertThat(byPass[0].passLength, equalTo(pass.passLength))
            assertThat(byPass[0].price, equalTo(pass.price))
            assertThat(byPass[0].generateAt, equalTo(pass.generateAt))
            assertThat(byPass[0].activeAt, equalTo(null))
            assertThat(byPass[0].expiredAt, equalTo(null))
        }
    }
}