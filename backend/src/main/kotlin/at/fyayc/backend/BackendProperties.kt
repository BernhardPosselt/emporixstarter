package at.fyayc.backend

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "backdend")
data class BackendProperties(
    val users: Users,
    val corsDomains: List<String>,
) {
    data class Users(val actuator: User) {
        data class User(val login: String, val password: String)
    }
}
