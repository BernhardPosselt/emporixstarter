package at.fyayc.backend.security.auth.password

import at.fyayc.backend.security.auth.EmporixLoginService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class EmporixPasswordLoginAuthenticationProvider(
    private val emporixLoginService: EmporixLoginService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication is UsernamePasswordAuthenticationToken) {
            val (user, token) = emporixLoginService.login(authentication.name, authentication.credentials.toString())
            return UsernamePasswordAuthenticationToken(user, token, user.authorities)
        } else {
            throw RuntimeException("authentication passed into EmporixPasswordLoginAuthenticationProvider must be of type UsernamePasswordAuthenticationToken")
        }
    }

    override fun supports(authentication: Class<*>) =
        authentication == UsernamePasswordAuthenticationToken::class.java
}