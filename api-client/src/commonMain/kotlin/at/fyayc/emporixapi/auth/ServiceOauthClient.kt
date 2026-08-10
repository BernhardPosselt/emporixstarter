package at.fyayc.emporixapi.auth

import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOrThrow
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.time.Clock

class ServiceOauthClient(
    private val client: HttpClient,
    private val apiConfig: ApiConfig,
) {
    suspend fun login(additionalScopes: List<String>): LeasedServiceToken {
        val scopes = additionalScopes + "tenant=${apiConfig.tenant}"
        val response = client.post(apiConfig.baseUrl) {
            url {
                appendPathSegments("oauth", "token")
                parameters {
                    append("tenant", apiConfig.tenant)
                    append("client_id", apiConfig.clientId)
                    append("client_secret", apiConfig.clientSecret)
                    append("grant_type", "client_credentials")
                    append("scope", scopes.joinToString(" "))
                }
            }
            contentType(ContentType.Application.Json)
        }.parseOrThrow<EmporixServiceToken>()
        return LeasedServiceToken(
            createdAt = Clock.System.now(),
            token = response.body,
        )
    }

    suspend fun refresh(token: LeasedServiceToken): LeasedServiceToken =
        login(
            token.token.scope.split(" ")
                .filterNot { it.startsWith("tentant=") })
}