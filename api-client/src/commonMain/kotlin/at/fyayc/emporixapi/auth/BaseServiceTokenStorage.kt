package at.fyayc.emporixapi.auth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class BaseServiceTokenStorage(
    val oauthClient: ServiceOauthClient,
    marginInSeconds: Int,
) : BaseTokenStorage<EmporixServiceToken, LeasedServiceToken>(marginInSeconds) {
    private val mutex = Mutex()
    private val mutex2 = Mutex()

    /**
     * Assumptions:
     * * You can lease more than one valid service token
     * * The service token is stored in memory and is leased on server start, so we do not need distributed locking
     */
    override suspend fun lockingRefresh(token: LeasedServiceToken): LeasedServiceToken {
        return mutex.withLock {
            val currentToken = load()
            if (isTokenExpired(currentToken)) {
                mutex2.withLock {
                    oauthClient.refresh(token)
                }
            } else {
                currentToken
            }
        }
    }
}