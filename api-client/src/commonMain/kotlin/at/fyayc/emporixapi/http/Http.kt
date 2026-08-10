package at.fyayc.emporixapi.http

import at.fyayc.emporixapi.auth.TokenStorage
import at.fyayc.emporixapi.auth.TokenUnavailable
import at.fyayc.emporixapi.oe.hmacSignatureB64
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.content.*
import io.ktor.util.*
import kotlin.io.encoding.Base64

private const val HMAC_SIGNATURE = "hmacSignature"
private const val TOKEN_TYPE = "tokenType"

fun HttpClient.registerInterceptors(
    sessionTokenStorage: TokenStorage,
    serviceTokenStorage: TokenStorage,
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
                    TokenType.SESSION -> sessionTokenStorage.retrieve()
                    TokenType.SERVICE -> serviceTokenStorage.retrieve()
                } ?: throw TokenUnavailable(type)
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