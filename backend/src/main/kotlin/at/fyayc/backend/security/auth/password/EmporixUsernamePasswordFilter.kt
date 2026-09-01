package at.fyayc.backend.security.auth.password

import at.fyayc.backend.security.auth.CustomerAuthenticationSuccessHandler
import at.fyayc.backend.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecodingException
import kotlinx.serialization.json.decodeFromStream
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher


class EmporixUsernamePasswordFilter(
    private val json: Json,
    customerAuthenticationSuccessHandler: CustomerAuthenticationSuccessHandler,
) : AbstractAuthenticationProcessingFilter(
    PathPatternRequestMatcher.withDefaults()
        .matcher(HttpMethod.POST, "/login"),
) {
    init {
        setAuthenticationSuccessHandler(customerAuthenticationSuccessHandler)
    }

    companion object {
        val log = logger()
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        // TODO: what do we return on a success? or a failure?
        if (request.contentType != MediaType.APPLICATION_JSON_VALUE) {
            val exception = AuthenticationServiceException("Only JSON POST requests are supported to log in")
            log.error(exception.message, exception)
            throw exception
        }
        val credentials = try {
            json.decodeFromStream<PasswordLogin>(request.inputStream)
        } catch (e: JsonDecodingException) {
            val msg = "Failed to parse JSON from request"
            log.error(msg, e)
            throw AuthenticationServiceException(msg, e)
        }
        val authRequest = UsernamePasswordAuthenticationToken(credentials.email, credentials.password)
        authRequest.details = this.authenticationDetailsSource.buildDetails(request)
        return this.getAuthenticationManager().authenticate(authRequest)
    }
}