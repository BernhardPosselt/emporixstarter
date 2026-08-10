package at.fyayc.backend.emporixapi

import at.fyayc.backend.BackendProperties
import at.fyayc.emporixapi.auth.AnonymousOAuthClient
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.ServiceOauthClient
import at.fyayc.emporixapi.auth.ServiceTokenStorage
import at.fyayc.emporixapi.http.ApiConfig
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
    fun apiConfig(properties: BackendProperties) = ApiConfig(
        tenant = properties.tenant,
        clientId = properties.oauth.clientId,
        clientSecret = properties.oauth.clientSecret,
        clientScopes = properties.oauth.clientScopes,
    )

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
    }

    @Bean
    fun serviceOAuthClient(
        apiConfig: ApiConfig,
        httpClient: HttpClient,
    ) = ServiceOauthClient(
        client = httpClient,
        apiConfig = apiConfig
    )

    @Bean
    fun serviceTokenStorage(
        properties: BackendProperties,
        serviceOauthClient: ServiceOauthClient,
    ) = ServiceTokenStorage(
        oauthClient = serviceOauthClient,
        marginInSeconds = properties.oauth.refreshMarginInSeconds,
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
    fun redisLockRegistry(
        redisConnectionFactory: RedisConnectionFactory,
    ) = RedisLockRegistry(
        redisConnectionFactory,
        "oauth"
    )
}