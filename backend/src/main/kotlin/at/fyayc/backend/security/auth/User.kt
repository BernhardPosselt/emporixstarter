package at.fyayc.backend.security.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    val userId: String,
    val groups: List<UserGroup>,
    val isActive: Boolean,
    val isOnHold: Boolean,
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = groups

    override fun getPassword() = null

    override fun getUsername(): String = userId

    override fun isEnabled() = isActive && !isOnHold
}