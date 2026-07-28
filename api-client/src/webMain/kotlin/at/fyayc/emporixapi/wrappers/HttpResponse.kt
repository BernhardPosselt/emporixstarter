package at.fyayc.emporixapi.wrappers

import io.ktor.client.call.*
import io.ktor.client.statement.*

@JsExport
interface HttpResponse {
    val statusCode: Int
}

@JsExport
data class ApiResponse<T>(override val statusCode: Int, val body: T) : HttpResponse

// note that ApiError and toJS() are generic solutions; you can create your own ApiError subclass with a typed body instead
@JsExport
open class ApiError(override val statusCode: Int, message: String) : HttpResponse, RuntimeException(message = message)

/**
 * Generic mapper for exceptions. If you need to support a typed exception, subclass ApiError and
 * call this function like:
 * response.toJs { if(status.value == 400) TypedApiError(body<TheType>()) else {...}}
 */
suspend inline fun <reified T> io.ktor.client.statement.HttpResponse.toJs(
    errorHandler: suspend io.ktor.client.statement.HttpResponse.() -> ApiError = {
        ApiError(
            status.value,
            bodyAsText()
        )
    }
) = when (val code = status.value) {
    in 200..399 -> ApiResponse(code, body<T>())
    else -> throw errorHandler()
}