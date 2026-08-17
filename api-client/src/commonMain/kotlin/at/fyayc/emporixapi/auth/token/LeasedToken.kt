package at.fyayc.emporixapi.auth.token

import kotlin.time.Instant

interface LeasedToken<T : OAuthToken> {
    val token: T
    val createdAt: Instant
}