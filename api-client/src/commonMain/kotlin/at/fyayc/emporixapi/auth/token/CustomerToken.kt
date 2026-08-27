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
    @SerialName("session_id")
    override val sessionId: String,
    @SerialName("saas_token")
    val saasToken: String,
    val initialPassword: Boolean,
    // deprecated properties
    @Deprecated("use _ ones")
    @SerialName("accessToken")
    val oldAccessToken: String,
    @Deprecated("use _ ones")
    @SerialName("expiresIn")
    val oldExpiresIn: Int,
    @Deprecated("use _ ones")
    @SerialName("refreshToken")
    val oldRefreshToken: String,
    @Deprecated("use _ ones")
    @SerialName("refreshTokenExpiresIn")
    val oldRefreshTokenExpiresIn: Int,
    @Deprecated("use _ ones")
    @SerialName("saasToken")
    val oldSaasToken: String,
) : SessionToken

