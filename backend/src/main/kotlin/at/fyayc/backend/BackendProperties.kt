package at.fyayc.backend

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "backend")
data class BackendProperties(
    val users: Users,
    val corsDomains: List<String>,
    val tenant: String,
    val emporixApi: EmporixApi
) {
    data class Users(val actuator: User) {
        data class User(val login: String, val password: String)
    }

    data class EmporixApi(
        val timeoutMillis: Long,
        val oauth: OAuth,
    ) {
        data class OAuth(
            val storefront: OAuthClient,
        ) {
            data class OAuthClient(
                val clientId: String,
                val clientSecret: String,
                val clientScopes: Map<String, String>,
                val refreshMarginInSeconds: Int,
            )
        }
    }
}
