package at.fyayc.emporixapi.auth

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LeasedSessionToken(
    override val token: EmporixSessionToken,
    override val createdAt: Instant,
    val type: SessionTokenType,
) : LeasedToken<EmporixSessionToken>