package at.fyayc.backend.emporixapi

import at.fyayc.backend.BackendProperties
import at.fyayc.backend.toClientConfig
import at.fyayc.emporixapi.auth.AnonymousOAuthClient
import at.fyayc.emporixapi.auth.CustomerOAuthClient
import at.fyayc.emporixapi.auth.IAMClient
import at.fyayc.emporixapi.auth.ServiceOauthClient
import at.fyayc.emporixapi.customer.CustomerClient
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.registerOEInterceptors
import at.fyayc.emporixapi.session.SessionClient
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
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
    )

    @Bean
    fun httpClient(properties: BackendProperties) = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = properties.emporixApi.timeoutMillis
        }
        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
                // enable this for forwards compatibility; downside is that optional properties that were mistyped
                // won't be fixed
                // ignoreUnknownKeys = true
            })
        }

        install(Logging) {
        }
    }.also { it.registerOEInterceptors() }

    @Bean
    fun storefrontOAuthClient(
        apiConfig: ApiConfig,
        httpClient: HttpClient,
        properties: BackendProperties,
    ) = ServiceOauthClient(
        client = httpClient,
        apiConfig = apiConfig,
        oauthClientConfig = properties.emporixApi.oauth.storefront.toClientConfig()
    )

    @Bean
    fun storefrontTokenStorage(
        storefrontOAuthClient: ServiceOauthClient,
        properties: BackendProperties,
    ) = ServiceTokenStorage(
        oauthClient = storefrontOAuthClient,
        refreshMarginInSeconds = properties.emporixApi.oauth.refreshMarginInSeconds,
    )

    @Bean
    fun emporixOAuthClient(
        apiConfig: ApiConfig,
        httpClient: HttpClient,
        properties: BackendProperties,
    ) = ServiceOauthClient(
        client = httpClient,
        apiConfig = apiConfig,
        oauthClientConfig = properties.emporixApi.oauth.emporix.toClientConfig()
    )

    @Bean
    fun emporixTokenStorage(
        emporixOAuthClient: ServiceOauthClient,
        properties: BackendProperties,
    ) = ServiceTokenStorage(
        oauthClient = emporixOAuthClient,
        refreshMarginInSeconds = properties.emporixApi.oauth.refreshMarginInSeconds,
    )

    @Bean
    fun anonymousOAuthClient(
        apiConfig: ApiConfig,
        httpClient: HttpClient,
        properties: BackendProperties,
    ) = AnonymousOAuthClient(
        client = httpClient,
        apiConfig = apiConfig,
        storefrontClientId = properties.emporixApi.oauth.storefront.clientId,
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

    @Bean
    fun iamClient(
        apiConfig: ApiConfig,
        httpClient: HttpClient,
    ) = IAMClient(
        client = httpClient,
        apiConfig = apiConfig,
    )
}