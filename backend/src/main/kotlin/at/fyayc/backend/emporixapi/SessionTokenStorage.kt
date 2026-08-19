package at.fyayc.backend.emporixapi

import at.fyayc.backend.BackendProperties
import at.fyayc.emporixapi.auth.AnonymousOAuthClient
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.token.LeasedAnonymousToken
import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import at.fyayc.emporixapi.auth.token.LeasedSessionToken
import at.fyayc.emporixapi.auth.token.SessionToken
import jakarta.servlet.http.HttpSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.springframework.integration.redis.util.RedisLockRegistry
import org.springframework.stereotype.Service

@Service
class SessionTokenStorage(
    properties: BackendProperties,
    httpSession: HttpSession,
    private val anonymousOAuthClient: AnonymousOAuthClient,
    private val customerOAuthClient: CustomerOAuthClient,
    private val redisLockRegistry: RedisLockRegistry,
    private val json: Json,
) : BaseTokenStorage<SessionToken, LeasedSessionToken>(properties.emporixApi.oauth.refreshMarginInSeconds) {
    private var sessionToken by httpSession.property<String?>("EMPORIX_SESSION_TOKEN")

    override fun load(): LeasedSessionToken {
        val token = sessionToken
        return if (token == null) {
            runBlocking(Dispatchers.Default) {
                anonymousOAuthClient.login().also {
                    store(it)
                }
            }
        } else {
            json.decodeFromString(token)
        }
    }

    override fun store(token: LeasedSessionToken) {
        sessionToken = json.encodeToString(token)
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