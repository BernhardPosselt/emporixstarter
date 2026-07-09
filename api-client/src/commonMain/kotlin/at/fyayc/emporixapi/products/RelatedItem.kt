package at.fyayc.emporixapi.products

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
data class RelatedItem(
    val refId: String,
    val type: String
)