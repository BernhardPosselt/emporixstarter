package at.fyayc.emporixapi.auth

import kotlin.time.Instant

interface LeasedToken<T : OAuthToken> {
    val token: T
    val createdAt: Instant
}