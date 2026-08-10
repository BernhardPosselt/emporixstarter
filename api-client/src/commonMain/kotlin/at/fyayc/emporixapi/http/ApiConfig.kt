package at.fyayc.emporixapi.http

data class ApiConfig(
    val tenant: String,
    val clientId: String,
    val clientSecret: String,
    val clientScopes: Map<String, String>,
    val baseUrl: String = "https://api.emporix.io/",
)