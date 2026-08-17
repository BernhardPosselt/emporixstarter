package at.fyayc.emporixapi.auth.token

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
@SerialName("customer")
data class LeasedCustomerToken(
    override val token: CustomerToken,
    override val createdAt: Instant,
) : LeasedSessionToken