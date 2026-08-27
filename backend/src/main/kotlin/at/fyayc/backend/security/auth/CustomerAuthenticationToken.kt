package at.fyayc.backend.security.auth

import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils

class CustomerAuthenticationToken(
    private val user: User?,
    val token: LeasedCustomerToken,
    authorities: Collection<GrantedAuthority>?,
) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials() = null

    override fun getPrincipal() = user

    init {
        super.setAuthenticated(this.authorities != AuthorityUtils.NO_AUTHORITIES)
    }
}