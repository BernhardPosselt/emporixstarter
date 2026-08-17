package at.fyayc.backend.security.auth.password

import kotlinx.serialization.Serializable

@Serializable
data class PasswordLogin(
    val email: String,
    val password: String,
)