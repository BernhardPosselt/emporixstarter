package at.fyayc.emporixapi.auth

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LeasedCustomerToken(
    override val token: CustomerToken,
    override val createdAt: Instant,
) : LeasedSessionToken