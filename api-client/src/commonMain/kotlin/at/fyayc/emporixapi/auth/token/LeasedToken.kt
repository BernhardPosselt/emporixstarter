package at.fyayc.emporixapi.auth.token

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

interface LeasedToken<T : OAuthToken> {
    val token: T
    val createdAt: Instant

    fun isTokenExpired(margin: Duration): Boolean =
        (createdAt + token.expiresIn.seconds + margin) > Clock.System.now()

    fun isRefreshTokenExpired(margin: Duration): Boolean =
        (createdAt + token.refreshTokenExpiresIn.seconds + margin) > Clock.System.now()
}