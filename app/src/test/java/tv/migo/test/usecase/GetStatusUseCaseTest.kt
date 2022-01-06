package tv.migo.test.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import tv.migo.test.api.model.MigoStatus
import tv.migo.test.repo.StatusRepository
import tv.migo.test.utils.Status

class GetStatusUseCaseTest {

    @MockK
    lateinit var repo: StatusRepository

    private val getStatusUseCase by lazy {
        GetStatusUseCase(repo)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getStatus_Success_flow() = runBlocking {
        coEvery { repo.getStatus() } returns Response.success(MigoStatus(200, "OK"))
        val flow = getStatusUseCase.getStatus()
        flow.collect {
            coVerify(exactly = 1) {
                repo.getStatus()
            }
        }
        val list = flow.toList()
        assert(list[0].status == Status.SUCCESS)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getStatus_Fail_flow() = runBlocking {
        coEvery { repo.getStatus() } returns Response.error(403, "error".toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull()))
        val flow = getStatusUseCase.getStatus()
        flow.collect {
            coVerify(exactly = 1) {
                repo.getStatus()
            }
        }
        val list = flow.toList()
        assert(list[0].status == Status.ERROR)
    }
}