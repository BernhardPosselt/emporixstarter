package at.fyayc.backend.auth

import kotlinx.serialization.Serializable

@Serializable
data class UsernamePasswordLogin(
    val username: String,
    val password: String,
)