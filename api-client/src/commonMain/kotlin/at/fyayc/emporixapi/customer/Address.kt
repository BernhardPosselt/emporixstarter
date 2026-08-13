package at.fyayc.emporixapi.customer

import at.fyayc.emporixapi.session.CountryIso
import at.fyayc.emporixapi.session.Metadata
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Address(
    val id: String,
    val contactName: String,
    val companyName: String?,
    val street: String?,
    val streetNumber: String?,
    val streetAppendix: String?,
    val extraLine1: String?,
    val extraLine2: String?,
    val extraLine3: String?,
    val extraLine4: String?,
    val zipCode: String?,
    val city: String?,
    val country: CountryIso?,
    val state: String?,
    val contactPhone: String?,
    val isDefault: Boolean,
    val tags: List<String> = emptyList(),
    val metadata: Metadata,
    val mixins: JsonObject?,
)