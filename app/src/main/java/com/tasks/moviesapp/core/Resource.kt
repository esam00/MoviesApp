package com.tasks.moviesapp.core

data class Resource<out T>(val status: Status, val data: T? = null, val message: String? = null) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = Status.SUCCESS, data = data)

        fun <T> error(message: String, data: T? = null): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message)

        fun <T> loading(): Resource<T> =
            Resource(status = Status.LOADING)
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
