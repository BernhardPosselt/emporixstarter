package at.fyayc.backend.security.auth.password

import at.fyayc.backend.emporixapi.SessionTokenStorage
import at.fyayc.backend.security.auth.AlreadyLoggedInException
import at.fyayc.backend.security.auth.EmporixLoginService
import at.fyayc.emporixapi.auth.token.LeasedAnonymousToken
import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

class EmporixPasswordLoginAuthenticationProvider(
    private val emporixLoginService: EmporixLoginService,
    private val sessionTokenStorage: SessionTokenStorage,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        return if (authentication is UsernamePasswordAuthenticationToken) {
            val anonymousToken = when (val sessionToken = sessionTokenStorage.retrieve()) {
                is LeasedAnonymousToken -> sessionToken.token
                is LeasedCustomerToken -> throw AlreadyLoggedInException("Can not log again in with valid customer token")
            }
            val (user, token) = emporixLoginService.login(
                authentication.name,
                authentication.credentials.toString(),
                anonymousToken
            )
            UsernamePasswordAuthenticationToken(user, token, user.authorities)
        } else {
            null
        }
    }

    override fun supports(authentication: Class<*>) =
        authentication == UsernamePasswordAuthenticationToken::class.java
}