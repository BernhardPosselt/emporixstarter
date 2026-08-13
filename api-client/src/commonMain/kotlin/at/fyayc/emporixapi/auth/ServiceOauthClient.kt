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
    suspend fun login(): LeasedServiceToken {
        val response = client.post(apiConfig.baseUrl) {
            url {
                appendPathSegments("oauth", "token")
                parameters {
                    append("tenant", apiConfig.tenant)
                    append("client_id", apiConfig.clientId)
                    append("client_secret", apiConfig.clientSecret)
                    append("grant_type", "client_credentials")
                    append(
                        "scope", apiConfig.clientScopes
                            .entries
                            .joinToString(" ") { (key, value) -> "$key=$value" })
                }
            }
            contentType(ContentType.Application.Json)
        }.parseOrThrow<ServiceToken>()
        return LeasedServiceToken(
            createdAt = Clock.System.now(),
            token = response,
        )
    }
}