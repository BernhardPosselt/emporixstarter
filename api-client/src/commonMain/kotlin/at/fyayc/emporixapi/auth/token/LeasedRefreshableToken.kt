package at.fyayc.emporixapi.auth.token

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface LeasedRefreshableToken<T : SessionToken> : LeasedToken<T> {
    fun isRefreshTokenExpired(margin: Duration): Boolean =
        (createdAt + token.refreshTokenExpiresIn.seconds + margin) > Clock.System.now()
}