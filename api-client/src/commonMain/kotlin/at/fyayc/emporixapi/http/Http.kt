package at.fyayc.emporixapi.http

import at.fyayc.emporixapi.auth.LeasedToken
import at.fyayc.emporixapi.auth.OAuthToken
import at.fyayc.emporixapi.auth.TokenInaccessible
import at.fyayc.emporixapi.auth.TokenStorage
import at.fyayc.emporixapi.oe.hmacSignatureB64
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.content.*
import io.ktor.util.*
import kotlin.io.encoding.Base64
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

private const val HMAC_SIGNATURE = "hmacSignature"
private const val TOKEN_TYPE = "tokenType"

private fun <T : OAuthToken> refreshIfValidLessThanSeconds(
    token: LeasedToken<T>,
    marginInSeconds: Int,
    refresh: (String) -> LeasedToken<T>
): LeasedToken<T> {
    val now = Clock.System.now()
    // FIXME: refresh need distributed locking through spring and redis
    return if (token.createdAt.plus(marginInSeconds.seconds) > now) {
        refresh(token.token.refreshToken)
    } else {
        token
    }
}

internal fun HttpClient.registerInterceptors(
    storage: TokenStorage,
    oauthClient: OAuthClient,
    tokenRefreshDelta: Int,
) {
    plugin(HttpSend).intercept {
        it.attributes.getOrNull(AttributeKey<HmacSignature>(HMAC_SIGNATURE))
            ?.let { sig ->
                val body = it.body as TextContent
                val hmac = hmacSignatureB64(sig.secret, body.text)
                it.headers.append(sig.header, Base64.encode(hmac))
            }
        it.attributes.getOrNull(AttributeKey<TokenType>(TOKEN_TYPE))
            ?.let { type ->
                val token = when (type) {
                    TokenType.SESSION -> {
                        val token = storage.retrieveSessionToken()?.let {
                            if (tokenIsAtLeastValidForSeconds(tokenRefreshDelta)) {
                                it
                            } else {
                                oauthClient.refreshSessionToken(token)
                            }
                        } ?: throw TokenInaccessible(type)
                        storage.storeSessionToken(token)
                    }

                    TokenType.SERVICE -> {
                        val token = storage.retrieveServiceToken()?.let {
                            if (tokenIsAtLeastValidForSeconds(tokenRefreshDelta)) {
                                it
                            } else {
                                oauthClient.refreshSessionToken(token)
                            }
                        } ?: throw TokenInaccessible(type)
                        oauthClient.refreshServiceToken(token)
                        storage.storeServiceToken(token)
                    }
                }
                it.headers.append("Authorization", "Bearer $token")
            }
        execute(it)
    }
}

internal fun HttpRequestBuilder.withHmac(hmac: HmacSignature) {
    attributes.put(AttributeKey(HMAC_SIGNATURE), hmac)
}

enum class TokenType {
    SESSION,
    SERVICE;
}

internal fun HttpRequestBuilder.withToken(tokenType: TokenType) {
    attributes.put(AttributeKey(TOKEN_TYPE), tokenType)
}