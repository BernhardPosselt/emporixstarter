package at.fyayc.backend.security.auth.password

import at.fyayc.backend.security.auth.EmporixLoginService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

class EmporixPasswordLoginAuthenticationProvider(
    private val emporixLoginService: EmporixLoginService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        return if (authentication is UsernamePasswordAuthenticationToken) {
            val (user, token) = emporixLoginService.login(authentication.name, authentication.credentials.toString())
            UsernamePasswordAuthenticationToken(user, token, user.authorities)
        } else {
            null
        }
    }

    override fun supports(authentication: Class<*>) =
        authentication == UsernamePasswordAuthenticationToken::class.java
}