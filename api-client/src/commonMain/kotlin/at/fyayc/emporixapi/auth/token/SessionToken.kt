package at.fyayc.emporixapi.auth.token

import kotlinx.serialization.Serializable

@Serializable
sealed interface SessionToken : OAuthToken {
    val sessionId: String
    override val refreshToken: String
    override val refreshTokenExpiresIn: Int
}