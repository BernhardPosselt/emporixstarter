package at.fyayc.backend.emporixapi

import at.fyayc.emporixapi.auth.token.LeasedToken
import at.fyayc.emporixapi.auth.token.OAuthToken
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

abstract class BaseTokenStorage<T : OAuthToken, LT : LeasedToken<T>>(
    private val marginInSeconds: Int,
) {
    fun retrieve(): LeasedToken<T> {
        // TODO: do we want to translate exceptions from failed tokens here?
        val currentToken = load()
        return if (isTokenExpired(currentToken)) {
            lockingRefresh(currentToken)
            load()
        } else {
            currentToken
        }
    }

    protected fun isTokenExpired(token: LT): Boolean =
        token.createdAt.plus(marginInSeconds.seconds) > Clock.System.now()

    protected abstract fun load(): LT
    protected abstract fun store(token: LT)
    protected abstract fun lockingRefresh(token: LT)
}