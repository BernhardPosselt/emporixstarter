package at.fyayc.backend.security.auth.sso

import at.fyayc.backend.security.auth.CustomerAuthenticationToken
import at.fyayc.backend.security.auth.EmporixLoginService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

class EmporixSSOAuthenticationProvider(
    private val emporixLoginService: EmporixLoginService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        return if (authentication is CustomerAuthenticationToken) {
            val (user, token) = emporixLoginService.login(
                authentication.token,
            )
            CustomerAuthenticationToken(user, token, user.authorities)
        } else {
            null
        }
    }

    override fun supports(authentication: Class<*>) =
        authentication == CustomerAuthenticationToken::class.java
}