package at.fyayc.emporixapi.http

data class ApiConfig(
    val tenant: String,
    val baseUrl: String = "https://api.emporix.io/",
)