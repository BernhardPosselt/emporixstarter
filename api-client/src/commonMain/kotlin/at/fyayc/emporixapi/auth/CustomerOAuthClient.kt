package at.fyayc.emporixapi.auth

import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOrThrow
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.time.Clock

class CustomerOAuthClient(
    private val client: HttpClient,
    private val apiConfig: ApiConfig,
) {
    suspend fun login(credentials: CustomerCredentials): LeasedSessionToken {
        val response = client.post(apiConfig.baseUrl) {
            url {
                appendPathSegments("customer", apiConfig.tenant, "login")
            }
            setBody(credentials)
            contentType(ContentType.Application.Json)
        }.parseOrThrow<EmporixSessionToken>()
        return LeasedSessionToken(
            createdAt = Clock.System.now(),
            token = response.body,
            type = SessionTokenType.CUSTOMER,
        )
    }

    suspend fun refresh(token: LeasedSessionToken): LeasedSessionToken {
        val response = client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("customer", apiConfig.tenant, "refreshauthtoken")
                parameters {
                    append("refresh_token", token.token.refreshToken)
                }
            }
            contentType(ContentType.Application.Json)
        }.parseOrThrow<EmporixSessionToken>()
        return LeasedSessionToken(
            createdAt = Clock.System.now(),
            token = response.body,
            type = SessionTokenType.CUSTOMER,
        )
    }
}