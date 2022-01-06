package tv.migo.test.repo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tv.migo.test.db.MigoDatabase
import tv.migo.test.db.dao.PassDao
import tv.migo.test.db.data.PassType
import tv.migo.test.db.entity.PassInfo
import tv.migo.test.utils.alignToEndOfTheDay
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class PassRepoImplTest {
    private lateinit var passDao: PassDao
    private lateinit var db: MigoDatabase

    private val pass: PassInfo = PassInfo(
        passType = PassType.DAY,
        passLength = 1,
        price = 500,
        generateAt = System.currentTimeMillis()
    )

    private val pass2: PassInfo = PassInfo(
        passType = PassType.HOUR,
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
    fun testAddAndGetPass(){
        runBlocking {
            val passRepo = PassRepoImpl(passDao)
            passRepo.addPass(pass)

            val byPass = passDao.getPasses()
            assertThat(byPass[0].passType, equalTo(pass.passType))
            assertThat(byPass[0].passLength, equalTo(pass.passLength))
            assertThat(byPass[0].price, equalTo(pass.price))
            assertThat(byPass[0].generateAt, equalTo(pass.generateAt))
            assertThat(byPass[0].activeAt, equalTo(null))
            assertThat(byPass[0].expiredAt, equalTo(null))
        }
    }

    @Test
    fun testActivatePass(){
        runBlocking {
            val passRepo = PassRepoImpl(passDao)
            passRepo.addPass(pass)
            passRepo.activatePass(pass)

            val byPass = passDao.getPasses()
            assertThat(byPass[0].passType, equalTo(pass.passType))
            assertThat(byPass[0].passLength, equalTo(pass.passLength))
            assertThat(byPass[0].price, equalTo(pass.price))
            assertThat(byPass[0].generateAt, equalTo(pass.generateAt))
            assertNotNull(byPass[0].activeAt)
            assertNotNull(byPass[0].expiredAt)

            //DAY by the end of the day
            val expected = Calendar.getInstance().apply {
                timeInMillis = byPass[0].activeAt!!
                add(Calendar.DAY_OF_MONTH, byPass[0].passLength)
            }.alignToEndOfTheDay().timeInMillis
            assertThat(byPass[0].expiredAt, equalTo(expected))
        }
    }

    @Test
    fun testActivatePass2(){
        runBlocking {
            val passRepo = PassRepoImpl(passDao)
            passRepo.addPass(pass2)
            passRepo.activatePass(pass2)

            val byPass = passDao.getPasses()
            assertNotNull(byPass[0].activeAt)
            assertNotNull(byPass[0].expiredAt)

            //DAY by the end of the day
            val expected = Calendar.getInstance().apply {
                timeInMillis = byPass[0].activeAt!!
                add(Calendar.HOUR, byPass[0].passLength)
            }.timeInMillis
            assertThat(byPass[0].expiredAt, equalTo(expected))
        }
    }
}