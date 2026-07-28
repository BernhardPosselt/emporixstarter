package at.fyayc.emporixapi.wrappers.oe

import at.fyayc.emporixapi.wrappers.HttpResponse
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


