package tv.migo.test.utils

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val exception: Exception? = null
) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }

        fun <T> error(exception: Exception? = null, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, exception)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data)
        }

        fun <T> intermediate(data: T?): Resource<T> {
            return Resource(Status.INTERMEDIATE, data)
        }
    }
}

enum class Status {
    SUCCESS, ERROR, LOADING, INTERMEDIATE
}