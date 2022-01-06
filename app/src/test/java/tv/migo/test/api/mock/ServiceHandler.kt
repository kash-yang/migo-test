package tv.migo.test.api.mock

abstract class ServiceHandler {
    abstract fun handleUri(uri: String): String?

    fun interrupt(uri: String): Boolean {
        return handleUri(uri) != null
    }
}
