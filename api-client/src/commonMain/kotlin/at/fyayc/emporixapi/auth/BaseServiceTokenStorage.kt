package at.fyayc.emporixapi.auth

abstract class BaseServiceTokenStorage(
    val oauthClient: ServiceOauthClient,
    val distributedLock: DistributedLock,
    marginInSeconds: Int,
) : BaseTokenStorage<EmporixServiceToken, LeasedServiceToken>(marginInSeconds) {
    override suspend fun lockingRefresh(token: LeasedServiceToken): LeasedServiceToken {
        return distributedLock.locking("checkServiceTokenRefresh:${token.token.accessToken}") {
            val currentToken = this.load()
            if (tokenExpired(currentToken)) {
                distributedLock.locking("serviceTokenRefresh:${token.token.accessToken}") {
                    oauthClient.refresh(token)
                }
            } else {
                currentToken
            }
        }
    }
}