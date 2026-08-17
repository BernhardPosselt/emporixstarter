package at.fyayc.backend.security.auth

import org.springframework.security.core.GrantedAuthority

data class UserGroup(
    val id: String,
    val name: String,
) : GrantedAuthority {
    override fun getAuthority(): String = id
}