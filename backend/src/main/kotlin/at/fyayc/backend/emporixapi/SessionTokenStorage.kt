package at.fyayc.backend.emporixapi

import at.fyayc.backend.BackendProperties
import at.fyayc.emporixapi.auth.AnonymousOAuthClient
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.EmporixSessionToken
import at.fyayc.emporixapi.auth.LeasedSessionToken
import at.fyayc.emporixapi.auth.SessionTokenType
import org.springframework.stereotype.Service

@Service
class SessionTokenStorage(
    properties: BackendProperties,
    private val anonymousOAuthClient: AnonymousOAuthClient,
    private val customerOAuthClient: CustomerOAuthClient,
    private val distributedLock: RedisDistributedLock,
) : BaseTokenStorage<EmporixSessionToken, LeasedSessionToken>(properties.emporixApi.oauth.refreshMarginInSeconds) {
    override fun load(): LeasedSessionToken {
        TODO("Not yet implemented")
    }

    override fun store(token: LeasedSessionToken) {
        TODO("Not yet implemented")
    }

    override fun lockingRefresh(token: LeasedSessionToken) {
        return distributedLock.locking("checkSessionTokenRefresh:${token.type}:${token.token.accessToken}") {
            val currentToken = this.load()
            if (isTokenExpired(currentToken)) {
                distributedLock.locking("sessionTokenRefresh:${token.type}:${token.token.accessToken}") {
                    store(
                        when (token.type) {
                            SessionTokenType.CUSTOMER -> customerOAuthClient.refresh(token)
                            SessionTokenType.ANONYMOUS -> anonymousOAuthClient.refresh(token)
                        }
                    )
                }
            }
        }
    }
}