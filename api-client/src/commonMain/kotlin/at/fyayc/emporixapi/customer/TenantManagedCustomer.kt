package at.fyayc.emporixapi.customer

import at.fyayc.emporixapi.session.Currency
import at.fyayc.emporixapi.session.LanguageIso
import at.fyayc.emporixapi.session.Metadata
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlin.time.Instant

@Serializable
data class TenantManagedCustomer(
    val title: String?,
    val firstName: String?,
    val middleName: String?,
    val lastName: String?,
    val contactPhone: String?,
    val company: String?,
    val preferredLanguage: LanguageIso?,
    val preferredCurrency: Currency?,
    val preferredSite: String?,
    val type: CustomerType = CustomerType.CUSTOMER,
    val photoUrl: String?,
    val b2b: JsonObject?,
    val metadata: Metadata,
    val mixins: JsonObject?,
    val restriction: String?,
    val customerNumber: String,
    val id: String,
    val defaultAddress: Address,
    val accounts: List<AccountId> = emptyList(),
    val contactEmail: String?,
    val active: Boolean = false,
    val onHold: Boolean = false,
    val email: String,
    val businessModel: BusinessModel = BusinessModel.B2C,
    val metadataCreatedAt: Instant,
)