package at.fyayc.emporixapi.session

import at.fyayc.emporixapi.i18n.CountryIso
import at.fyayc.emporixapi.i18n.CurrencyIso
import at.fyayc.emporixapi.i18n.LanguageIso
import at.fyayc.emporixapi.site.SiteCode
import kotlinx.serialization.json.JsonObject

data class CustomerSession(
    val sessionId: String?,
    val customerId: String?,
    val siteCode: SiteCode?,
    val currency: CurrencyIso?,
    val cartId: String?,
    val targetLocation: CountryIso?,
    val language: LanguageIso?,
    val metadata: Metadata,
    val context: JsonObject?, // TODO: can probably be typed to a more specific type
)