package at.fyayc.emporixapi.auth

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@Serializable
enum class SessionTokenType {
    CUSTOMER,
    ANONYMOUS;
}

interface LeasedToken<T : OAuthToken> {
    val token: T
    val createdAt: Instant
}

@Serializable
data class LeasedSessionToken(
    override val token: EmporixSessionToken,
    override val createdAt: Instant,
    val type: SessionTokenType,
) : LeasedToken<EmporixSessionToken>

@Serializable
data class LeasedServiceToken(
    override val token: EmporixServiceToken,
    override val createdAt: Instant,
) : LeasedToken<EmporixServiceToken>

interface TokenStorage {
    suspend fun retrieve(): String?
}

abstract class BaseTokenStorage<T : OAuthToken, LT : LeasedToken<T>>(
    private val marginInSeconds: Int,
) : TokenStorage {
    override suspend fun retrieve(): String? {
        val currentToken = load()
        return if (tokenExpired(currentToken)) {
            val newToken = lockingRefresh(currentToken)
            store(newToken)
            newToken.token.accessToken
        } else {
            currentToken.token.accessToken
        }
    }

    protected fun tokenExpired(token: LT): Boolean =
        token.createdAt.plus(marginInSeconds.seconds) > Clock.System.now()

    protected abstract suspend fun load(): LT
    protected abstract suspend fun store(token: LT)
    protected abstract suspend fun lockingRefresh(token: LT): LT
}

abstract class SessionTokenStorage(
    marginInSeconds: Int,

    ) : BaseTokenStorage<EmporixSessionToken, LeasedSessionToken>(marginInSeconds) {
    override suspend fun load(): LeasedSessionToken {
        TODO("Not yet implemented")
    }

    override suspend fun store(token: LeasedSessionToken) {
        TODO("Not yet implemented")
    }

    override suspend fun lockingRefresh(token: LeasedSessionToken): LeasedSessionToken {
        TODO("Not yet implemented")
    }
}


class ServiceTokenStorage(
    marginInSeconds: Int,
) : BaseTokenStorage<EmporixServiceToken, LeasedServiceToken>(marginInSeconds) {
    override suspend fun load(): LeasedServiceToken {
        TODO("Not yet implemented")
    }

    override suspend fun store(token: LeasedServiceToken) {
        TODO("Not yet implemented")
    }

    override suspend fun lockingRefresh(token: LeasedServiceToken): LeasedServiceToken {
        TODO("Not yet implemented")
    }
}