package at.fyayc.emporixapi.auth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ServiceTokenStorage(
    val oauthClient: ServiceOauthClient,
    marginInSeconds: Int,
) : BaseTokenStorage<EmporixServiceToken, LeasedServiceToken>(marginInSeconds) {
    private val mutex = Mutex()
    private var token: LeasedServiceToken? = null

    override suspend fun load(): LeasedServiceToken {
        val curr = this.token
        return curr
            ?: mutex.withLock {
                val token = this.token
                if (token == null) {
                    val new = oauthClient.login()
                    store(new)
                    new
                } else {
                    token
                }
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
    override suspend fun lockingRefresh(token: LeasedServiceToken) {
        return mutex.withLock {
            if (isTokenExpired(load())) {
                store(oauthClient.login())
            }
        }
    }
}