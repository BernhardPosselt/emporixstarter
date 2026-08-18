package at.fyayc.backend.security.auth.sso

import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils

class SSOLoginToken(
    private val principalObj: Any? = null,
    private val credentialsObj: LeasedCustomerToken,
    authorities: Collection<GrantedAuthority>? = null
) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials() = credentialsObj

    override fun getPrincipal() = principalObj

    init {
        super.setAuthenticated(this.authorities != AuthorityUtils.NO_AUTHORITIES)
    }
}