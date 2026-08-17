package at.fyayc.backend.security.auth.sso

import at.fyayc.emporixapi.auth.token.CustomerToken
import kotlinx.serialization.Serializable

@Serializable
data class SSOLogin(
    val token: CustomerToken,
)