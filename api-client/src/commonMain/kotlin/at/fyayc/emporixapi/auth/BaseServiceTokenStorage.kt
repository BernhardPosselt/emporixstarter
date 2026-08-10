package at.fyayc.emporixapi.auth

abstract class BaseServiceTokenStorage(
    val oauthClient: ServiceOauthClient,
    marginInSeconds: Int,
) : BaseTokenStorage<EmporixServiceToken, LeasedServiceToken>(marginInSeconds) {
    /**
     * Assumptions:
     * * You can lease more than one valid token
     * * The service token is stored in memory and is leased on server start, so we do not need distributed locking
     */
    override suspend fun lockingRefresh(token: LeasedServiceToken): LeasedServiceToken {
        return oauthClient.refresh(token)
    }
}