package at.fyayc.emporixapi.wrappers

import at.fyayc.emporixapi.http.ApiError
import io.ktor.client.call.*
import io.ktor.client.statement.*


/**
 * Generic mapper for exceptions. If you need to support a typed exception, subclass ApiError and
 * call this function like:
 * response.toJs { if(status.value == 400) TypedApiError(body<TheType>()) else {...}}
 */
suspend inline fun <reified T> HttpResponse.toJs(
    errorHandler: suspend HttpResponse.() -> ApiError = {
        ApiError(
            status.value,
            bodyAsText()
        )
    }
) = when (val code = status.value) {
    in 200..399 -> body<T>()
    else -> throw errorHandler()
}