package tv.migo.test.api.services

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tv.migo.test.api.MoshiProvider
import java.io.IOException

class TestMigoStatusService {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: MigoStatusService

    private val raw = """
            {
                "status": 200,
                "message": "OK"
            }
        """.trimIndent()

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(MoshiProvider.moshi))
            .build()
            .create(MigoStatusService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun test_status_get() {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(
            mockResponse.setBody(raw)
        )

        runBlocking {
            val status = service.getStatus().body()
            assert(status != null)
            assert(status?.status == 200)
            assert(status?.message == "OK")
        }
    }
}