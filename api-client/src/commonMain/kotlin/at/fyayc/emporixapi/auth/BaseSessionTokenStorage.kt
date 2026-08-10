package at.fyayc.emporixapi.auth

abstract class BaseSessionTokenStorage(
    val anonymousOauthClient: AnonymousOauthClient,
    val customerOauthClient: CustomerOauthClient,
    val distributedLock: DistributedLock,
    marginInSeconds: Int,
) : BaseTokenStorage<EmporixSessionToken, LeasedSessionToken>(marginInSeconds) {
    override suspend fun lockingRefresh(token: LeasedSessionToken): LeasedSessionToken {
        return distributedLock.locking("checkSessionTokenRefresh:${token.type}:${token.token.accessToken}") {
            val currentToken = this.load()
            if (tokenExpired(currentToken)) {
                distributedLock.locking("sessionTokenRefresh:${token.type}:${token.token.accessToken}") {
                    when (token.type) {
                        SessionTokenType.CUSTOMER -> customerOauthClient.refresh(token)
                        SessionTokenType.ANONYMOUS -> anonymousOauthClient.refresh(token)
                    }
                }
            } else {
                currentToken
            }
        }
    }
}