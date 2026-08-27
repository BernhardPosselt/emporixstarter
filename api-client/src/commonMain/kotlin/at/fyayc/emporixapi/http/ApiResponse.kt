package at.fyayc.emporixapi.http

import io.ktor.client.call.*
import io.ktor.client.statement.*

/**
 * Generic mapper for exceptions. If you need to support a typed exception, subclass ApiError and
 * call this function like:
 * response.toJs { if(status.value == 400) TypedApiError(body<TheType>()) else {...}}
 */
suspend inline fun <reified T> HttpResponse.parseOrThrow(
    noinline errorHandler: suspend HttpResponse.() -> ApiError = {
        ApiError(
            status.value,
            bodyAsText()
        )
    }
) = when (status.value) {
    in 200..399 -> body<T>()
    else -> throw errorHandler()
}

/**
 * Similar to parseOrThrow but maps a 404 onto a nullable type
 */
suspend inline fun <reified T> HttpResponse.parseOptionalOrThrow(
    noinline errorHandler: suspend HttpResponse.() -> ApiError = {
        ApiError(
            status.value,
            bodyAsText()
        )
    }
) = when (val code = status.value) {
    in 200..399 -> body<T>()
    404 -> null
    else -> throw errorHandler()
}