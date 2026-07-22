package at.fyayc.emporixapi.oe

internal expect suspend fun hmacSignatureB64(secret: String, data: String): ByteArray