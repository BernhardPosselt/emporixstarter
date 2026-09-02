package at.fyayc.backend.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.context.SecurityContextRepository

/**
 * auth filters need to be registered using a custom DSL since the
 * authenticationManager instance is not yet available yet
 * see https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter#disqus_thread
 * also see: https://medium.com/@persolenom/are-you-using-component-in-spring-security-filters-stop-now-its-wrong-and-dangerous-801f6671a2f9
 */
class AuthenticationFilterDsl(
    vararg val filters: () -> AbstractAuthenticationProcessingFilter,
) : AbstractHttpConfigurer<AuthenticationFilterDsl, HttpSecurity>() {
    override fun configure(http: HttpSecurity) {
        val authenticationManager = http.getSharedObject(AuthenticationManager::class.java)
        val repository = http.getSharedObject(SecurityContextRepository::class.java)
        val strategy = http.getSharedObject(SessionAuthenticationStrategy::class.java)
        val rememberMeServices = http.getSharedObject(RememberMeServices::class.java)
        filters.forEach {
            val filter = it()
            filter.setAuthenticationManager(authenticationManager)
            filter.setSecurityContextRepository(repository)
            filter.setSessionAuthenticationStrategy(strategy)
            rememberMeServices?.let { remember -> filter.rememberMeServices = remember }
            postProcess(filter)
            http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)
        }
    }
}