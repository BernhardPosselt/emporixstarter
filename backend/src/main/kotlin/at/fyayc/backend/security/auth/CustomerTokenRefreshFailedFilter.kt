package at.fyayc.backend.security.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.filter.OncePerRequestFilter

class CustomerTokenRefreshFailedFilter : OncePerRequestFilter() {
    private val securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy()
    private val logoutHandler = SecurityContextLogoutHandler()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (_: CustomerTokenRefreshFailed) {
            val auth = this.securityContextHolderStrategy.context.authentication
            logoutHandler.logout(request, response, auth)
            // TODO: this does not work if the response is streamed since headers are already sent
            response.status = HttpStatus.UNAUTHORIZED.value()
        }
    }
}