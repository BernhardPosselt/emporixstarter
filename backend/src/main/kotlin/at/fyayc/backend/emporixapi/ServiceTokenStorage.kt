package at.fyayc.backend.emporixapi

import at.fyayc.backend.BackendProperties
import at.fyayc.emporixapi.auth.EmporixServiceToken
import at.fyayc.emporixapi.auth.LeasedServiceToken
import at.fyayc.emporixapi.auth.ServiceOauthClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class ServiceTokenStorage(
    val oauthClient: ServiceOauthClient,
    properties: BackendProperties,
) : BaseTokenStorage<EmporixServiceToken, LeasedServiceToken>(properties.emporixApi.oauth.refreshMarginInSeconds) {
    private var token: LeasedServiceToken? = null

    override fun load(): LeasedServiceToken {
        val curr = this.token
        return curr
            ?: synchronized(this) {
                val token = this.token
                if (token == null) {
                    val new = newToken()
                    store(new)
                    new
                } else {
                    token
                }
            }
    }

    override fun store(token: LeasedServiceToken) {
        this.token = token
    }

    /**
     * Assumptions:
     * * You can lease more than one valid service token
     * * The service token is stored in memory, so we do not need distributed locking
     */
    override fun lockingRefresh(token: LeasedServiceToken) {
        return synchronized(this) {
            if (isTokenExpired(load())) {
                store(newToken())
            }
        }
    }

    private fun newToken() = runBlocking(Dispatchers.Default) {
        oauthClient.login()
    }
}