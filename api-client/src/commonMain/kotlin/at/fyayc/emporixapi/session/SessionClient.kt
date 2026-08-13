package at.fyayc.emporixapi.session

import at.fyayc.emporixapi.auth.EmporixSessionToken
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.ApiResponse
import at.fyayc.emporixapi.http.parseOrThrow
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class SessionClient(
    private val client: HttpClient,
    private val apiConfig: ApiConfig,
) {
    suspend fun ownSessionContext(
        token: EmporixSessionToken,
    ): ApiResponse<CustomerSession> {
        return client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("session-context", apiConfig.tenant, "me", "context")
            }
            contentType(ContentType.Application.Json)
            bearerAuth(token.accessToken)
        }.parseOrThrow<CustomerSession>()
    }
}

