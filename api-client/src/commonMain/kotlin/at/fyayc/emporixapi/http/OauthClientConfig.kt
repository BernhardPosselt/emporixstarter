package at.fyayc.emporixapi.http

data class OauthClientConfig(
    val id: String,
    val secret: String,
    val scopes: List<String>,
)