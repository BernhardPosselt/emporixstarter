package at.fyayc.emporixapi.products

import at.fyayc.emporixapi.i18n.TranslatedValue
import at.fyayc.emporixapi.i18n.CountryIso
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonObject
import kotlin.js.JsExport

@JsonClassDiscriminator("productType")
@Serializable
@JsExport
sealed interface CreateProduct

@Serializable
@SerialName("BASIC")
@JsExport
data class CreateBasicProduct(
    val code: String,
    val name: TranslatedValue<String>? = null,
    val description: TranslatedValue<String>? = null,
    val published: Boolean? = null,
    val template: Template? = null,
    val taxClasses: Map<CountryIso, String>? = null,
    val relatedItems: List<RelatedItem>? = null,
    val mixins: JsonObject? = null,
    val weightDependent: Boolean? = null,
    val brandId: String? = null,
    val labelIds: List<String>? = null,
    val metadata: Metadata? = null,
    val id: String? = null,
) : CreateProduct

@Serializable
@SerialName("BUNDLE")
@JsExport
data class CreateBundleProduct(
    val code: String,
    val bundledProducts: List<BundledProduct>,
    val name: Map<String, String>? = null,
    val description: Map<String, String>? = null,
    val published: Boolean? = null,
    val template: Template? = null,
    val taxClasses: Map<String, String>? = null,
    val relatedItems: List<RelatedItem>? = null,
    val mixins: Map<String, String>? = null,
    val weightDependent: Boolean? = null,
    val brandId: String? = null,
    val labelIds: List<String>? = null,
    val metadata: Metadata? = null,
    val id: String? = null,
) : CreateProduct