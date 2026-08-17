package at.fyayc.backend.security.auth.sso

import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher
import kotlin.time.Clock

class SsoLoginFilter(
    private val json: Json,
    authenticationManager: AuthenticationManager,
) : AbstractAuthenticationProcessingFilter(
    PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/sso"),
    authenticationManager,
) {
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        // TODO: what do we return on a success? or a failure?
        if (request.contentType != MediaType.APPLICATION_JSON_VALUE) {
            throw AuthenticationServiceException("Only JSON POST requests are supported to log in")
        }
        val credentials = json.decodeFromStream<SSOLogin>(request.inputStream)
        val authRequest = SSOLoginToken(
            credentials = LeasedCustomerToken(
                token = credentials.token,
                createdAt = Clock.System.now(),
            )
        )
        authRequest.details = this.authenticationDetailsSource.buildDetails(request)
        return this.getAuthenticationManager().authenticate(authRequest)
    }
}
