package at.fyayc.emporixapi.auth.token

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnonymousToken(
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
    val scope: String,
    @SerialName("session_id")
    override val sessionId: String,
    @Deprecated("Provided as session_id")
    // deprecated properties
    @SerialName("sessionId")
    val oldSessionId: String,
) : SessionToken