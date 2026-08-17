package at.fyayc.emporixapi.auth.token

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomerToken(
    @SerialName("token_type")
    override val tokenType: String,
    @SerialName("access_token")
    override val accessToken: String,
    @SerialName("expires_in")
    override val expiresIn: Int,
    @SerialName("refresh_token")
    override val refreshToken: String,
    @SerialName("refresh_token_expires_in")
    override val refreshTokenExpiresIn: Int,
    override val scope: String,
    val sessionId: String,
    val saasToken: String,
    val initialPassword: Boolean,
) : SessionToken

