package at.fyayc.emporixapi.auth

import kotlinx.serialization.Serializable

@Serializable
data class CustomerCredentials(
    val email: String,
    val password: String,
)