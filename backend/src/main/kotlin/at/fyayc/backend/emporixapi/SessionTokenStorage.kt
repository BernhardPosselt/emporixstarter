package at.fyayc.backend.emporixapi

import at.fyayc.backend.BackendProperties
import at.fyayc.emporixapi.auth.AnonymousOAuthClient
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.LeasedAnonymousToken
import at.fyayc.emporixapi.auth.LeasedCustomerToken
import at.fyayc.emporixapi.auth.LeasedSessionToken
import at.fyayc.emporixapi.auth.SessionToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.integration.redis.util.RedisLockRegistry
import org.springframework.stereotype.Service

@Service
class SessionTokenStorage(
    properties: BackendProperties,
    private val anonymousOAuthClient: AnonymousOAuthClient,
    private val customerOAuthClient: CustomerOAuthClient,
    private val redisLockRegistry: RedisLockRegistry,
) : BaseTokenStorage<SessionToken, LeasedSessionToken>(properties.emporixApi.oauth.refreshMarginInSeconds) {
    override fun load(): LeasedSessionToken {
        TODO("Not yet implemented")
    }

    override fun store(token: LeasedSessionToken) {
        TODO("Not yet implemented")
    }

    override fun lockingRefresh(token: LeasedSessionToken) {
        // FIXME: handle invalid token auths and add better Exception generic
        val type = when (token) {
            is LeasedAnonymousToken -> "anonymous"
            is LeasedCustomerToken -> "customer"
        }
        redisLockRegistry.executeLocked<Exception>("checkSessionTokenRefresh:${type}:${token.token.accessToken}") {
            val currentToken = this.load()
            if (isTokenExpired(currentToken)) {
                store(
                    when (token) {
                        is LeasedCustomerToken -> runBlocking(Dispatchers.Default) {
                            customerOAuthClient.refresh(
                                token.token
                            )
                        }

                        is LeasedAnonymousToken -> runBlocking(Dispatchers.Default) {
                            anonymousOAuthClient.refresh(
                                token.token
                            )
                        }
                    }
                )
            }
        }
    }
}