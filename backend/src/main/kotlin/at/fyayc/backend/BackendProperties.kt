package at.fyayc.backend

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "backend")
data class BackendProperties(
    val users: Users,
    val corsDomains: List<String>,
    val oauth: OAuth,
    val tenant: String,
    val emporixApi: EmporixApi
) {
    data class Users(val actuator: User) {
        data class User(val login: String, val password: String)
    }

    data class OAuth(
        val clientId: String,
        val clientSecret: String,
        val clientScopes: Map<String, String>,
        val refreshMarginInSeconds: Int,
    )

    data class EmporixApi(
        val timeoutMillis: Long,
    )
}
