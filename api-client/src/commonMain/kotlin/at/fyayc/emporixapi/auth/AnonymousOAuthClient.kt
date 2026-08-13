package at.fyayc.emporixapi.auth

import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOrThrow
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.time.Clock

class AnonymousOAuthClient(
    private val client: HttpClient,
    private val apiConfig: ApiConfig,
) {
    suspend fun login(): LeasedAnonymousToken {
        val response = client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("customerlogin", "auth", "anonymous", "login")
                parameters {
                    append("tenant", apiConfig.tenant)
                    append("client_id", apiConfig.clientId)
                }
            }
            contentType(ContentType.Application.Json)
        }.parseOrThrow<AnonymousToken>()
        return LeasedAnonymousToken(
            createdAt = Clock.System.now(),
            token = response,
        )
    }

    suspend fun refresh(token: AnonymousToken): LeasedAnonymousToken {
        val response = client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("customerlogin", "auth", "anonymous", "refresh")
                parameters {
                    append("tenant", apiConfig.tenant)
                    append("client_id", apiConfig.clientId)
                    append("anonymous_token", token.accessToken)
                    append("refresh_token", token.refreshToken)
                }
            }
            contentType(ContentType.Application.Json)
        }.parseOrThrow<AnonymousToken>()
        return LeasedAnonymousToken(
            createdAt = Clock.System.now(),
            token = response,
        )
    }
}