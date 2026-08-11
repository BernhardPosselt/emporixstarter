package at.fyayc.emporixapi.auth

import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

abstract class BaseTokenStorage<T : OAuthToken, LT : LeasedToken<T>>(
    private val marginInSeconds: Int,
) : TokenStorage {
    override suspend fun retrieve(): String? {
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

    protected abstract suspend fun load(): LT
    protected abstract suspend fun store(token: LT)
    protected abstract suspend fun lockingRefresh(token: LT)
}