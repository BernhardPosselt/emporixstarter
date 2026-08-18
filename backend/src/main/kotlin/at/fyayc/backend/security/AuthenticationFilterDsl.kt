package at.fyayc.backend.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

typealias AuthenticationFilterFactory = (AuthenticationManager) -> AbstractAuthenticationProcessingFilter

class AuthenticationFilterDsl(
    val filters: List<AuthenticationFilterFactory>,
) : AbstractHttpConfigurer<AuthenticationFilterDsl, HttpSecurity>() {
    override fun configure(http: HttpSecurity) {
        val authenticationManager = http.getSharedObject(AuthenticationManager::class.java)
        filters.forEach {
            val filter = it(authenticationManager)
            http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)
        }
    }
}