package at.fyayc.backend

import at.fyayc.emporixapi.http.ApiConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiClients {
    @Bean
    fun apiConfig(properties: BackendProperties) = ApiConfig(
        tenant = properties.tenant,
        clientId = properties.oauth.clientId,
        clientSecret = properties.oauth.clientSecret,
        clientScopes = properties.oauth.clientScopes,
    )
}