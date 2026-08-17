package at.fyayc.emporixapi.session

import at.fyayc.emporixapi.util.LanguageIso
import kotlinx.serialization.json.JsonObject

data class CustomerSession(
    val sessionId: String?,
    val customerId: String?,
    val siteCode: String?,
    val currency: Currency?,
    val cartId: String?,
    val targetLocation: CountryIso?,
    val language: LanguageIso?,
    val metadata: Metadata,
    val context: JsonObject?, // TODO: can probably be typed to a more specific type
)