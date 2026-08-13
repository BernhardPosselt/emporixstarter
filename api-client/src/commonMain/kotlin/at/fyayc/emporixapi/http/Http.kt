package at.fyayc.emporixapi.http

import at.fyayc.emporixapi.oe.hmacSignatureB64
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.content.*
import io.ktor.util.*
import kotlin.io.encoding.Base64

private const val HMAC_SIGNATURE = "hmacSignature"

fun HttpClient.registerOEInterceptors() {
    plugin(HttpSend).intercept {
        it.attributes.getOrNull(AttributeKey<HmacSignature>(HMAC_SIGNATURE))
            ?.let { sig ->
                val body = it.body as TextContent
                val hmac = hmacSignatureB64(sig.secret, body.text)
                it.headers.append(sig.header, Base64.encode(hmac))
            }
        execute(it)
    }
}

internal fun HttpRequestBuilder.withHmac(hmac: HmacSignature) {
    attributes.put(AttributeKey(HMAC_SIGNATURE), hmac)
}