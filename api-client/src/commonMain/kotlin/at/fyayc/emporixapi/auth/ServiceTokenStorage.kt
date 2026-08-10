package at.fyayc.emporixapi.auth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ServiceTokenStorage(
    val oauthClient: ServiceOauthClient,
    marginInSeconds: Int,
) : BaseTokenStorage<EmporixServiceToken, LeasedServiceToken>(marginInSeconds) {
    private val mutex = Mutex()
    private val mutex2 = Mutex()
    private var token: LeasedServiceToken? = null

    override suspend fun load(): LeasedServiceToken {
        val token = token
        return if (token != null) {
            token
        } else {
            val new = newToken()
            store(new)
            new
        }
    }

    override suspend fun store(token: LeasedServiceToken) {
        this.token = token
    }

    /**
     * Assumptions:
     * * You can lease more than one valid service token
     * * The service token is stored in memory, so we do not need distributed locking
     */
    override suspend fun lockingRefresh(token: LeasedServiceToken): LeasedServiceToken {
        return mutex.withLock {
            val currentToken = load()
            if (isTokenExpired(currentToken)) {
                newToken()
            } else {
                currentToken
            }
        }
    }

    private suspend fun newToken() = mutex2.withLock {
        oauthClient.login()
    }
}