package at.fyayc.backend.emporixapi

import at.fyayc.backend.BackendProperties
import at.fyayc.emporixapi.auth.AnonymousOAuthClient
import at.fyayc.emporixapi.auth.BaseSessionTokenStorage
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.DistributedLock
import at.fyayc.emporixapi.auth.LeasedSessionToken
import org.springframework.stereotype.Service

@Service
class SessionTokenStorage(
    properties: BackendProperties,
    anonymousOAuthClient: AnonymousOAuthClient,
    customerOAuthClient: CustomerOAuthClient,
    distributedLock: DistributedLock,
) : BaseSessionTokenStorage(
    anonymousOauthClient = anonymousOAuthClient,
    customerOauthClient = customerOAuthClient,
    distributedLock = distributedLock,
    marginInSeconds = properties.oauth.refreshMarginInSeconds,

    ) {
    override suspend fun load(): LeasedSessionToken {
        TODO("Not yet implemented")
    }

    override suspend fun store(token: LeasedSessionToken) {
        TODO("Not yet implemented")
    }
}