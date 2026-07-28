package at.fyayc.emporixapi.wrappers.oe

import at.fyayc.emporixapi.wrappers.HttpResponse
import at.fyayc.emporixapi.wrappers.UnhandledResponseCode
import io.ktor.client.call.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@JsExport
sealed class OEResponse(override val statusCode: Int) : HttpResponse {
    class OEOkResponse(
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

    class OEBadRequestResponse : OEResponse(400)
    class OEForbiddenResponse : OEResponse(401)
}

suspend fun io.ktor.client.statement.HttpResponse.toJs() =
    when (val code = status.value) {
        200 -> OEResponse.OEOkResponse(code, body<OEResponse.OEOkResponse.Body>())
        400 -> OEResponse.OEBadRequestResponse()
        401 -> OEResponse.OEForbiddenResponse()
        else -> throw UnhandledResponseCode(code)
    }