package at.fyayc.emporixapi.products

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
@JsExport
data class BundledProduct(
    val productId: String,
    val amount: Int,
)
