package com.luas.tracker.common.util

/**
 *  Converts any non-null object to an object of type ApiResponse.Success
 */
fun <T> T.asSuccess(): ApiResponse<T> = ApiResponse.Success(this)

/**
 *  Converts any exception to an object of type ApiResponse.Error
 *  Typically we could logs these errors on a remote logging server.
 *  For sake of this test, I am only printing the stacktrace so that the error appears in logs
 */
fun <T> Throwable.asError(): ApiResponse<T> {
    return ApiResponse.Error(this)
}

/**
 *  This method takes a suspend function that returns data & converts the data to ApiResponse format
 *  This method also handles any exceptions thrown to let ViewModels interpret the Api response gracefully
 */
suspend fun <T> apiResponseFrom(block: suspend () -> T): ApiResponse<T> {
    return try {
        block.invoke().asSuccess()
    } catch (e: Exception) {
        e.asError()
    }
}

/**
 *  This method takes accepts ApiResponse class type & allows devs to avoid writing repetitive
 *  when clause. Instead, they can focus on logic than boiler plate code.
 */
fun <T> apiResponseHandler(
    status: ApiResponse<T>,
    onSuccess: (T) -> Unit,
    onError: (Throwable) -> Unit
) {
    when (status) {
        is ApiResponse.Success -> onSuccess.invoke(status.data)
        is ApiResponse.Error -> onError.invoke(status.throwable)
    }

}