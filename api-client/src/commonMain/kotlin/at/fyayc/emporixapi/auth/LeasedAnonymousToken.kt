package at.fyayc.emporixapi.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
@SerialName("anonymous")
data class LeasedAnonymousToken(
    override val token: AnonymousToken,
    override val createdAt: Instant,
) : LeasedSessionToken