package at.fyayc.backend.emporixapi

import at.fyayc.emporixapi.auth.LeasedToken
import at.fyayc.emporixapi.auth.OAuthToken
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

abstract class BaseTokenStorage<T : OAuthToken, LT : LeasedToken<T>>(
    private val marginInSeconds: Int,
) {
    fun retrieve(): String? {
        val currentToken = load()
        return if (isTokenExpired(currentToken)) {
            lockingRefresh(currentToken)
            load().token.accessToken
        } else {
            currentToken.token.accessToken
        }
    }

    protected fun isTokenExpired(token: LT): Boolean =
        token.createdAt.plus(marginInSeconds.seconds) > Clock.System.now()

    protected abstract fun load(): LT
    protected abstract fun store(token: LT)
    protected abstract fun lockingRefresh(token: LT)
}