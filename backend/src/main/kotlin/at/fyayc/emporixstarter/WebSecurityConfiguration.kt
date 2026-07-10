package at.fyayc.emporixstarter

import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration(proxyBeanMethods = false)
class WebSecurityConfiguration {
    @Bean
    fun passwordEncounter() = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    @Order(1)
    fun actuatorSecurity(
        http: HttpSecurity,
        properties: Properties,
        passwordEncoder: PasswordEncoder,
    ): SecurityFilterChain {
        val user = properties.users.actuator
        val userDetail = User.builder()
            .username(user.login)
            .password(passwordEncoder.encode(user.password))
            .roles("Actuator")
            .build()
        val manager = InMemoryUserDetailsManager(userDetail)

        http.invoke {
            securityMatcher(EndpointRequest.toAnyEndpoint())
            authorizeHttpRequests {
                authorize("/actuator/health", permitAll)
                authorize("/actuator/health/liveness", permitAll)
                authorize("/actuator/health/readiness", permitAll)
                authorize("/actuator/**", hasRole("Actuator"))
            }
            httpBasic { }
        }
        return http
            .userDetailsService(manager)
            .build()
    }

    @Bean
    @Order(2)
    fun apiSecurity(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            securityMatcher("/**")
            authorizeHttpRequests {
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                // allow cors
                authorize(HttpMethod.OPTIONS, "/**", permitAll)
                authorize(HttpMethod.GET, "/products/**", authenticated)
                authorize("/**", denyAll)
            }
            httpBasic { }
            sessionManagement {
                // no cookies
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            csrf {
                // no csrf possible without cookies
                disable()
            }
        }
        return http.build()
    }
}