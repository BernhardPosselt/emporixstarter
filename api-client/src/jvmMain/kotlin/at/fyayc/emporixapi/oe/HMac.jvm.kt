package at.fyayc.emporixapi.oe

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

actual suspend fun hmacSignatureB64(secret: String, data: String): ByteArray {
    val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(secretKey)
    return mac.doFinal(data.toByteArray())
}