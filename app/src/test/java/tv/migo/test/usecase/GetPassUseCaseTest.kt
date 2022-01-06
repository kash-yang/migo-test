package tv.migo.test.usecase

import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import tv.migo.test.repo.PassRepository

class GetPassUseCaseTest {

    @MockK
    lateinit var repo: PassRepository

    private val getPassUseCase by lazy {
        GetPassUseCase(repo)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_activatePass_Success_flow() = runBlocking {
        getPassUseCase.getPassesLiveData()
        coVerify(exactly = 1) {
            repo.getPasses()
        }
    }
}