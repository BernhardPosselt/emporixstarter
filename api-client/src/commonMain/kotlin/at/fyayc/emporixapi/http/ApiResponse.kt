package at.fyayc.emporixapi.http

import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlin.js.JsExport

@JsExport
data class ApiResponse<T>(override val statusCode: Int, val body: T) : StatusCode


/**
 * Generic mapper for exceptions. If you need to support a typed exception, subclass ApiError and
 * call this function like:
 * response.toJs { if(status.value == 400) TypedApiError(body<TheType>()) else {...}}
 */
suspend inline fun <reified T> HttpResponse.parseOrThrow(
    errorHandler: suspend HttpResponse.() -> ApiError = {
        ApiError(
            status.value,
            bodyAsText()
        )
    }
) = when (val code = status.value) {
    in 200..399 -> ApiResponse(code, body<T>())
    else -> throw errorHandler()
}