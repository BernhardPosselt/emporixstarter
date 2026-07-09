package at.fyayc.emporixapi.products

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
data class Template(
    val id: String,
    val version: String,
)