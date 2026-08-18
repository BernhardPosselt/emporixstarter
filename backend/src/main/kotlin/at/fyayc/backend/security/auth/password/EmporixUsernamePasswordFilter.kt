package at.fyayc.backend.security.auth.password

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher

class EmporixUsernamePasswordFilter(
    private val json: Json,
    authenticationManager: AuthenticationManager,
) : AbstractAuthenticationProcessingFilter(
    PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/login"),
    authenticationManager,
) {
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        // TODO: what do we return on a success? or a failure?
        if (request.contentType != MediaType.APPLICATION_JSON_VALUE) {
            throw AuthenticationServiceException("Only JSON POST requests are supported to log in")
        }
        val credentials = json.decodeFromStream<PasswordLogin>(request.inputStream)
        val authRequest = UsernamePasswordAuthenticationToken(credentials.email, credentials.password)
        authRequest.details = this.authenticationDetailsSource.buildDetails(request)
        return this.getAuthenticationManager().authenticate(authRequest)
    }
}