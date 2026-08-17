package at.fyayc.emporixapi.auth.token

import kotlinx.serialization.Serializable

@Serializable
sealed interface SessionToken : OAuthToken