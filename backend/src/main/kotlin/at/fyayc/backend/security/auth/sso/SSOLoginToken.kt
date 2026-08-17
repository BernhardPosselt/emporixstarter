package at.fyayc.backend.security.auth.sso

import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class SSOLoginToken(
    val principal: Any? = null,
    val credentials: LeasedCustomerToken,
    val authorities: Collection<GrantedAuthority>? = null
) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials() = credentials

    override fun getPrincipal() = principal

    init {
        super.setAuthenticated(this.authorities != null)
    }
}