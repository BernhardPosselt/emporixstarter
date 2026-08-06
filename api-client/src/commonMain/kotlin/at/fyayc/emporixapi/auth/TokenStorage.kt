package at.fyayc.emporixapi.auth

import kotlin.time.Instant

enum class SessionTokenType {
    CUSTOMER,
    ANONYMOUS;
}

interface LeasedToken<T : OAuthToken> {
    val token: T
    val createdAt: Instant
}

data class LeasedSessionToken(
    val token: EmporixSessionToken,
    val type: SessionTokenType,
    val createdAt: Instant,
)

data class LeasedServiceToken(
    val token: EmporixServiceToken,
    val createdAt: Instant,
)

interface TokenStorage {
    suspend fun retrieveSessionToken(): LeasedSessionToken?
    suspend fun retrieveServiceToken(): LeasedServiceToken?
    suspend fun storeSessionToken(token: EmporixSessionToken)
    suspend fun storeServiceToken(token: EmporixServiceToken)
}