package at.fyayc.emporixapi.auth

import kotlinx.serialization.Serializable

@Serializable
enum class SessionTokenType {
    CUSTOMER,
    ANONYMOUS;
}