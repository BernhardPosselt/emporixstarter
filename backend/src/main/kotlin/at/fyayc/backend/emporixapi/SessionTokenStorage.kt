package at.fyayc.backend.emporixapi

import at.fyayc.backend.BackendProperties
import at.fyayc.backend.security.auth.CustomerTokenRefreshFailed
import at.fyayc.backend.util.logger
import at.fyayc.emporixapi.auth.AnonymousOAuthClient
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.token.LeasedAnonymousToken
import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import at.fyayc.emporixapi.auth.token.LeasedSessionToken
import at.fyayc.emporixapi.auth.token.SessionToken
import at.fyayc.emporixapi.http.ApiError
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
    companion object {
        private val log = logger()
    }

    private var sessionToken by httpSession.property<String?>("EMPORIX_SESSION_TOKEN")

    override fun load(): LeasedSessionToken {
        val token = sessionToken
        return if (token == null) {
            newAnonymousToken()
        } else {
            json.decodeFromString(token)
        }
    }

    private fun newAnonymousToken(): LeasedAnonymousToken {
        val token = try {
            runBlocking(Dispatchers.Default) {
                anonymousOAuthClient.login()
            }
        } catch (e: ApiError) {
            log.error("Failed to retrieve a new anonymous token", e)
            throw e
        }
        store(token)
        return token
    }

    private fun refreshAnonymousToken(token: LeasedAnonymousToken): LeasedAnonymousToken {
        return try {
            runBlocking(Dispatchers.Default) {
                anonymousOAuthClient.refresh(
                    token.token
                )
            }
        } catch (e: ApiError) {
            log.error("Failed to refresh anonymous token, leasing a new one", e)
            newAnonymousToken()
        }
    }

    private fun refreshCustomerToken(token: LeasedCustomerToken): LeasedCustomerToken {
        return try {
            runBlocking(Dispatchers.Default) {
                customerOAuthClient.refresh(
                    token.token
                )
            }
        } catch (e: ApiError) {
            log.error("Failed to refresh customer token, logging out", e)
            throw CustomerTokenRefreshFailed(e)
        }
    }

    override fun store(token: LeasedSessionToken) {
        sessionToken = json.encodeToString(token)
    }

    override fun lockingRefresh(token: LeasedSessionToken) {
        val type = when (token) {
            is LeasedAnonymousToken -> "anonymous"
            is LeasedCustomerToken -> "customer"
        }
        redisLockRegistry.executeLocked<Exception>("checkSessionTokenRefresh:${type}:${token.token.accessToken}") {
            val currentToken = this.load()
            if (isRefreshTokenExpired(token)) {
                when (token) {
                    // if an anonymous refresh token expires, we must
                    // request a new one losing the current cart
                    is LeasedAnonymousToken -> newAnonymousToken()
                    is LeasedCustomerToken -> throw CustomerTokenRefreshFailed()
                }
            } else if (isTokenExpired(currentToken)) {
                store(
                    when (token) {
                        is LeasedCustomerToken -> refreshCustomerToken(token)
                        is LeasedAnonymousToken -> refreshAnonymousToken(token)
                    }
                )
            }
        }
    }
}