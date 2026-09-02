package at.fyayc.backend

import at.fyayc.emporixapi.http.OauthClientConfig
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "backend")
data class BackendProperties(
    val users: Users,
    val corsDomains: List<String>,
    val tenant: String,
    val emporixApi: EmporixApi,
    val emporixGroups: EmporixGroups,
) {
    data class EmporixGroups(val customer: String)
    data class Users(val actuator: User) {
        data class User(val login: String, val password: String)
    }

    data class EmporixApi(
        val timeoutMillis: Long,
        val oauth: OAuth,
    ) {
        data class OAuth(
            val refreshMarginInSeconds: Int,
            val storefront: OAuthClient,
            val emporix: OAuthClient,
        ) {
            data class OAuthClient(
                val clientId: String,
                val clientSecret: String,
                val clientScopes: List<String>,
            )
        }
    }
}

fun BackendProperties.EmporixApi.OAuth.OAuthClient.toClientConfig() =
    OauthClientConfig(
        id = clientId,
        secret = clientSecret,
        scopes = clientScopes,
    )