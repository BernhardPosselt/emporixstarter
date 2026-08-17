package at.fyayc.emporixapi.auth.token

import kotlinx.serialization.Serializable

@Serializable
enum class SessionTokenType {
    CUSTOMER,
    ANONYMOUS;
}