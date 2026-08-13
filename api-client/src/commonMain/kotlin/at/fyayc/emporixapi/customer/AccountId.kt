package at.fyayc.emporixapi.customer

import kotlinx.serialization.Serializable

@Serializable
data class AccountId(
    val id: String?,
    val providerId: String?,
)