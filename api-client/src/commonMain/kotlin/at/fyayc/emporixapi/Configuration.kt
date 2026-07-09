package at.fyayc.emporixapi

import io.ktor.client.*

data class Configuration(
    val endpoint: String,
    val tenant: String,
    val client: HttpClient,
)
