package at.fyayc.emporixstarter

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "emporixstarter")
data class Properties(
    val users: Users,
    val corsDomains: List<String>,
) {
    data class Users(val actuator: User) {
        data class User(val login: String, val password: String)
    }
}
