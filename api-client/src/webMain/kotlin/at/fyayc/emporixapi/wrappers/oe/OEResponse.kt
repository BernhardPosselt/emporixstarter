package at.fyayc.emporixapi.wrappers.oe

import at.fyayc.emporixapi.wrappers.HttpResponse
import at.fyayc.emporixapi.wrappers.UnhandledResponseCode
import io.ktor.client.call.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@JsExport
sealed class OEResponse(override val statusCode: Int) : HttpResponse {
    class Ok(
        statusCode: Int,
        val body: Body
    ) : OEResponse(statusCode) {
        @Serializable
        data class Body(
            val status: String,
            val message: String,
            @SerialName("request_id")
            val requestId: String,
        )
    }

    class BadRequest : OEResponse(400)
    class Forbidden : OEResponse(401)
}

suspend fun io.ktor.client.statement.HttpResponse.toJs() =
    when (val code = status.value) {
        200 -> OEResponse.Ok(code, body<OEResponse.Ok.Body>())
        400 -> OEResponse.BadRequest()
        401 -> OEResponse.Forbidden()
        else -> throw UnhandledResponseCode(code)
    }