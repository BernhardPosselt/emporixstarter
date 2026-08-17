package at.fyayc.emporixapi.auth.token

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LeasedServiceToken(
    override val token: ServiceToken,
    override val createdAt: Instant,
) : LeasedToken<ServiceToken>