package at.fyayc.emporixapi.wrappers.oe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JsExport
@Serializable
data class OEResponse(
    val status: String,
    val message: String,
    @SerialName("request_id")
    val requestId: String,
)

