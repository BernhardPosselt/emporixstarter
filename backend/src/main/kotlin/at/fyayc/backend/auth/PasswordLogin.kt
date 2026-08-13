package at.fyayc.backend.auth

import kotlinx.serialization.Serializable

@Serializable
data class PasswordLogin(
    val email: String,
    val password: String,
)