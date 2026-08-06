package at.fyayc.emporixapi.auth

interface OAuthToken {
    val tokenType: String?
    val accessToken: String
    val expiresIn: Int
    val refreshToken: String
    val refreshTokenExpiresIn: Int
    val scope: String?
}