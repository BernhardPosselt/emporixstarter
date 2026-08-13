package at.fyayc.emporixapi.auth

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LeasedAnonymousToken(
    override val token: AnonymousToken,
    override val createdAt: Instant,
) : LeasedSessionToken