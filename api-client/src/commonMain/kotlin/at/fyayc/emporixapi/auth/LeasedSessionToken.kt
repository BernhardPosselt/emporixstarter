package at.fyayc.emporixapi.auth

import kotlinx.serialization.Serializable

@Serializable
sealed interface LeasedSessionToken : LeasedToken<SessionToken>