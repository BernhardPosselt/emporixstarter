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
    suspend fun login(): LeasedSessionToken {
        val response = client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("customerlogin", "auth", "anonymous", "login")
                parameters {
                    append("tenant", apiConfig.tenant)
                    append("client_id", apiConfig.clientId)
                }
            }
            contentType(ContentType.Application.Json)
        }.parseOrThrow<EmporixSessionToken>()
        return LeasedSessionToken(
            createdAt = Clock.System.now(),
            token = response.body,
            type = SessionTokenType.ANONYMOUS,
        )
    }

    suspend fun refresh(token: LeasedSessionToken): LeasedSessionToken {
        val response = client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("customerlogin", "auth", "anonymous", "refresh")
                parameters {
                    append("tenant", apiConfig.tenant)
                    append("client_id", apiConfig.clientId)
                    append("anonymous_token", token.token.accessToken)
                    append("refresh_token", token.token.refreshToken)
                }
            }
            contentType(ContentType.Application.Json)
        }.parseOrThrow<EmporixSessionToken>()
        return LeasedSessionToken(
            createdAt = Clock.System.now(),
            token = response.body,
            type = SessionTokenType.ANONYMOUS,
        )
    }
}