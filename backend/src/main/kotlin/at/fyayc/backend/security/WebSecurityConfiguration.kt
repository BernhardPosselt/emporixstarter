package at.fyayc.backend.security

import at.fyayc.backend.BackendProperties
import at.fyayc.backend.emporixapi.SessionTokenStorage
import at.fyayc.backend.security.auth.CustomerAuthenticationSuccessHandler
import at.fyayc.backend.security.auth.CustomerTokenRefreshFailedFilter
import at.fyayc.backend.security.auth.EmporixLoginService
import at.fyayc.backend.security.auth.LoginSuccess
import at.fyayc.backend.security.auth.password.EmporixPasswordLoginAuthenticationProvider
import at.fyayc.backend.security.auth.password.EmporixUsernamePasswordFilter
import at.fyayc.backend.security.auth.password.PasswordLogin
import at.fyayc.backend.security.auth.sso.EmporixSSOAuthenticationProvider
import at.fyayc.backend.security.auth.sso.EmporixSSOFilter
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.utils.SpringDocAnnotationsUtils
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
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter
import org.springframework.security.web.session.SessionManagementFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration(proxyBeanMethods = false)
class WebSecurityConfiguration {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun passwordEncounter() = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    @Order(1)
    fun actuatorSecurity(
        http: HttpSecurity,
        backendProperties: BackendProperties,
        passwordEncoder: PasswordEncoder,
    ): SecurityFilterChain {
        val user = backendProperties.users.actuator
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
    fun apiSecurity(
        http: HttpSecurity,
        emporixLoginService: EmporixLoginService,
        sessionTokenStorage: SessionTokenStorage,
        customerAuthenticationSuccessHandler: CustomerAuthenticationSuccessHandler,
        json: Json,
        properties: BackendProperties,
    ): SecurityFilterChain {
        val groups = properties.emporixGroups
        http {
            securityMatcher("/**")
            authorizeHttpRequests {
                authorize(HttpMethod.POST, "/login", permitAll)
                // swagger docs
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                // allow cors
                authorize(HttpMethod.OPTIONS, "/**", permitAll)
                authorize(HttpMethod.GET, "/products/**", authenticated)
                authorize(HttpMethod.GET, "/profile/**", hasRole(groups.customer))
                authorize("/**", denyAll)
            }
            anonymous {

            }
            formLogin { }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.IF_REQUIRED
                sessionConcurrency {
                    maximumSessions = 1
                    maxSessionsPreventsLogin = true
                }
            }
            addFilterAfter<SessionManagementFilter>(CustomerTokenRefreshFailedFilter())
            logout {
                // TODO: needs to call https://developer.emporix.io/api-references/api-guides/companies-and-customers/customer-management/api-reference/authentication-and-authorization#get-customer-tenant-logout
                addLogoutHandler(HeaderWriterLogoutHandler(ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES)))
            }
            csrf {
                // we don't use non application/json routes
                disable()
            }
        }
        http.authenticationProvider(EmporixSSOAuthenticationProvider(emporixLoginService))
        http.authenticationProvider(
            EmporixPasswordLoginAuthenticationProvider(
                emporixLoginService,
                sessionTokenStorage
            )
        )
        http.with(
            AuthenticationFilterDsl(
                { EmporixSSOFilter(json, customerAuthenticationSuccessHandler) },
                { EmporixUsernamePasswordFilter(json, customerAuthenticationSuccessHandler) },
            )
        )
        return http.build()
    }

    @Bean
    fun corsConfigurer(locationServiceProperties: BackendProperties): WebMvcConfigurer {
        val domains = locationServiceProperties.corsDomains.map(String::trim)
        log.debug("Whitelisting CORS domains: ${domains.joinToString(", ")}")

        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                    .allowedHeaders("*") //
                    .allowedOrigins(*domains.toTypedArray())
                    .allowCredentials(true)
            }
        }
    }

    // see https://github.com/springdoc/springdoc-openapi/blob/64d512824d8e01f8ec4d8fa3510a6ecd8d40aa57/springdoc-openapi-starter-common/src/main/java/org/springdoc/core/configuration/SpringDocSecurityConfiguration.java#L108
    @Bean
    fun loginEndpoints(): OpenApiCustomizer = {
        val jsonMediaType = org.springframework.http.MediaType.APPLICATION_JSON_VALUE
        it.path(
            "/login", PathItem()
                .post(
                    Operation()
                        .tags(listOf("Login"))
                        .operationId("login")
                        .requestBody(
                            RequestBody()
                                .required(true)
                                .content(
                                    Content()
                                        .addMediaType(
                                            jsonMediaType, MediaType()
                                                .schema(
                                                    SpringDocAnnotationsUtils.resolveSchemaFromType(
                                                        PasswordLogin::class.java,
                                                        it.components,
                                                        null,
                                                    ).required(listOf("email", "password"))
                                                )
                                        )
                                )
                        )
                        .responses(
                            ApiResponses()
                                .addApiResponse(
                                    "200", ApiResponse()
                                        .description("Login a Customer via E-Mail and Password")
                                        .content(
                                            Content()
                                                .addMediaType(
                                                    jsonMediaType,
                                                    MediaType()
                                                        .schema(
                                                            SpringDocAnnotationsUtils.resolveSchemaFromType(
                                                                LoginSuccess::class.java,
                                                                it.components,
                                                                null,
                                                            ).required(listOf("languageIso", "currencyIso"))
                                                        )
                                                )
                                        )
                                )
                                .addApiResponse(
                                    "403", ApiResponse()
                                        .description("If anything fails")
                                )
                        )
                )
        )
    }
}