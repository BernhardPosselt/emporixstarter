package at.fyayc.backend.security.auth.sso

import at.fyayc.backend.security.auth.EmporixLoginService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class EmporixSSOAuthenticationProvider(
    private val emporixLoginService: EmporixLoginService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication is SSOLoginToken) {
            val (user, token) = emporixLoginService.login(
                authentication.credentials
            )
            return SSOLoginToken(user, token, user.authorities)
        } else {
            throw RuntimeException("authentication passed into EmporixPasswordLoginAuthenticationProvider must be of type SSOLoginToken")
        }
    }

    override fun supports(authentication: Class<*>) =
        authentication == SSOLoginToken::class.java
}