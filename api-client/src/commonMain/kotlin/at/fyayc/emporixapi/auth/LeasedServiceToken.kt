package at.fyayc.emporixapi.auth

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LeasedServiceToken(
    override val token: EmporixServiceToken,
    override val createdAt: Instant,
) : LeasedToken<EmporixServiceToken>