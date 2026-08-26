package at.fyayc.emporixapi.auth

import at.fyayc.emporixapi.auth.token.AnonymousToken
import at.fyayc.emporixapi.auth.token.LeasedAnonymousToken
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOrThrow
import at.fyayc.emporixapi.i18n.CountryIso
import at.fyayc.emporixapi.i18n.CurrencyIso
import at.fyayc.emporixapi.i18n.LanguageIso
import at.fyayc.emporixapi.site.SiteCode
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.time.Clock

class AnonymousOAuthClient(
    private val client: HttpClient,
    private val apiConfig: ApiConfig,
) {
    suspend fun login(
        language: LanguageIso? = null,
        currency: CurrencyIso? = null,
        siteCode: SiteCode? = null,
        targetLocation: CountryIso? = null,
    ): LeasedAnonymousToken {
        val response = client.get(apiConfig.baseUrl) {
            url {
                appendPathSegments("customerlogin", "auth", "anonymous", "login")
                parameters.append("client_id", apiConfig.clientId)
                parameters.append("tenant", apiConfig.tenant)
                language?.let { parameters.append("language", it.name) }
                currency?.let { parameters.append("currency", it.name) }
                siteCode?.let { parameters.append("siteCode", it.name) }
                targetLocation?.let { parameters.append("targetLocation", it.name) }
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
                parameters.append("tenant", apiConfig.tenant)
                parameters.append("client_id", apiConfig.clientId)
                parameters.append("anonymous_token", token.accessToken)
                parameters.append("refresh_token", token.refreshToken)
            }
            contentType(ContentType.Application.Json)
        }.parseOrThrow<AnonymousToken>()
        return LeasedAnonymousToken(
            createdAt = Clock.System.now(),
            token = response,
        )
    }
}