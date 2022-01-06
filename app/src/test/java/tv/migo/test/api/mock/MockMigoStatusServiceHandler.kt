package tv.migo.test.api.mock

class MockMigoStatusServiceHandler : ServiceHandler() {
    companion object {
        val serviceResponse = """
            {
                "status": 200,
                "message": "OK"
            }
        """.trimIndent()
    }
    override fun handleUri(uri: String): String? {
        println("status: ${uri.contains("status")}")
        return when {
            uri.contains("status") -> serviceResponse
            else -> null
        }
    }
}