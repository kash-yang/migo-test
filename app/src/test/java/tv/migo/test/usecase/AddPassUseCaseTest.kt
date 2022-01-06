package tv.migo.test.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import tv.migo.test.db.data.PassType
import tv.migo.test.db.entity.PassInfo
import tv.migo.test.repo.PassRepository
import tv.migo.test.utils.Status

class AddPassUseCaseTest {

    @MockK
    lateinit var repo: PassRepository

    private val addPassUseCase by lazy {
        AddPassUseCase(repo)
    }

    private val pass: PassInfo = PassInfo(
        passType = PassType.DAY,
        passLength = 1,
        price = 500,
        generateAt = System.currentTimeMillis()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_addPass_Success_flow() = runBlocking {
        coEvery { repo.addPass(any()) } returns 1
        val flow = addPassUseCase.addPass(pass)
        flow.collect {
            coVerify(exactly = 1) {
                repo.addPass(any())
            }
        }
        val list = flow.toList()
        assert(list[0].status == Status.LOADING)
        assert(list[1].status == Status.SUCCESS)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_addPass_Fail_flow() = runBlocking {
        coEvery { repo.addPass(any()) } returns -1
        val flow = addPassUseCase.addPass(pass)
        flow.collect {
            coVerify(exactly = 1) {
                repo.addPass(any())
            }
        }
        val list = flow.toList()
        assert(list[0].status == Status.LOADING)
        assert(list[1].status == Status.ERROR)
    }
}