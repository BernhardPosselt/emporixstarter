package at.fyayc.backend.security.auth

import at.fyayc.backend.emporixapi.SessionStorage
import at.fyayc.backend.emporixapi.SessionTokenStorage
import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import at.fyayc.emporixapi.session.SessionClient
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomerAuthenticationSuccessHandler(
    private val sessionClient: SessionClient,
    private val sessionStorage: SessionStorage,
    private val sessionTokenStorage: SessionTokenStorage,
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        if (authentication is UsernamePasswordAuthenticationToken) {
            val leasedCustomerToken = authentication.credentials as LeasedCustomerToken
            val emporixSession = runBlocking {
                sessionClient.ownSessionContext(leasedCustomerToken.token)
            }
            sessionStorage.language = emporixSession.language
            sessionStorage.sessionId = emporixSession.sessionId
            sessionStorage.customerId = emporixSession.customerId
            sessionStorage.siteCode = emporixSession.siteCode
            sessionStorage.currency = emporixSession.currency
            sessionStorage.cartId = emporixSession.cartId
            sessionStorage.targetLocation = emporixSession.targetLocation
            // TODO: custom session attributes are not persisted
            sessionTokenStorage.store(leasedCustomerToken)
        }
    }
}