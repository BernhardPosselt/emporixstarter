package at.fyayc.emporixapi.http

internal data class HmacSignature(
    val secret: String,
    val header: String,
)