package at.fyayc.backend.emporixapi

import at.fyayc.emporixapi.auth.token.LeasedToken
import at.fyayc.emporixapi.auth.token.OAuthToken
import kotlin.time.Duration.Companion.seconds

abstract class BaseTokenStorage<T : OAuthToken, LT : LeasedToken<T>>(
    protected val marginInSeconds: Int,
) {
    fun retrieve(): LT {
        val currentToken = load()
        return if (currentToken.isTokenExpired(marginInSeconds.seconds)) {
            lockingRefresh(currentToken)
            load()
        } else {
            currentToken
        }
    }

    abstract fun store(token: LT)

    protected abstract fun load(): LT
    protected abstract fun lockingRefresh(token: LT)
}