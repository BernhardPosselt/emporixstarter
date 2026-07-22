package at.fyayc.emporixapi.oe

import js.buffer.toByteArray
import web.crypto.*
import web.encoding.TextEncoder


internal actual suspend fun hmacSignatureB64(secret: String, data: String): ByteArray {
    // Encode the key and message as Uint8Array
    val encoder = TextEncoder()
    val messageData = encoder.encode(data)
    // Import the key
    val cryptoKey = crypto.subtle.importKey(
        KeyFormat.raw,
        encoder.encode(secret),
        HmacImportParams(
            name = "HMAC",
            hash = AlgorithmIdentifier("SHA-256")
        ),
        false,
        arrayOf(KeyUsage.sign)
    )
    // Generate HMAC
    val signature = crypto.subtle.sign(
        "HMAC",
        cryptoKey,
        messageData
    )
    return signature.toByteArray()
}

