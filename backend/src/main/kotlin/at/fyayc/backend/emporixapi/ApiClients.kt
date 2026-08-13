package at.fyayc.backend.emporixapi

import at.fyayc.backend.BackendProperties
import at.fyayc.emporixapi.auth.AnonymousOAuthClient
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.ServiceOauthClient
import at.fyayc.emporixapi.customer.CustomerClient
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.registerOEInterceptors
import at.fyayc.emporixapi.session.SessionClient
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.integration.redis.util.RedisLockRegistry

@Configuration
class ApiClients {
    @Bean
    fun apiConfig(properties: BackendProperties): ApiConfig {
        val oauth = properties.emporixApi.oauth
        return ApiConfig(
            tenant = properties.tenant,
            clientId = oauth.clientId,
            clientSecret = oauth.clientSecret,
            clientScopes = oauth.clientScopes,
        )
    }

    @Bean
    fun httpClient(properties: BackendProperties) = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = properties.emporixApi.timeoutMillis
        }
        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
            })
        }
    }.also { it.registerOEInterceptors() }

    @Bean
    fun serviceOAuthClient(
        apiConfig: ApiConfig,
        httpClient: HttpClient,
    ) = ServiceOauthClient(
        client = httpClient,
        apiConfig = apiConfig
    )

    @Bean
    fun anonymousOAuthClient(
        apiConfig: ApiConfig,
        httpClient: HttpClient,
    ) = AnonymousOAuthClient(
        client = httpClient,
        apiConfig = apiConfig,
    )

    @Bean
    fun customerOAuthClient(
        apiConfig: ApiConfig,
        httpClient: HttpClient,
    ) = CustomerOAuthClient(
        client = httpClient,
        apiConfig = apiConfig,
    )

    @Bean
    fun sessionClient(
        apiConfig: ApiConfig,
        httpClient: HttpClient,
    ) = SessionClient(
        client = httpClient,
        apiConfig = apiConfig,
    )

    @Bean
    fun customerClient(
        apiConfig: ApiConfig,
        httpClient: HttpClient,
    ) = CustomerClient(
        client = httpClient,
        apiConfig = apiConfig,
    )

    @Bean
    fun redisLockRegistry(
        redisConnectionFactory: RedisConnectionFactory,
    ) = RedisLockRegistry(
        redisConnectionFactory,
        "oauth"
    )
}