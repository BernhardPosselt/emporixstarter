package at.fyayc.backend

import at.fyayc.emporixapi.auth.BaseSessionTokenStorage
import at.fyayc.emporixapi.auth.ServiceTokenStorage
import at.fyayc.emporixapi.http.registerInterceptors
import io.ktor.client.*
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class InitializeHttpClient(
    val serviceTokenStorage: ServiceTokenStorage,
    val sessionTokenStorage: BaseSessionTokenStorage,
    val httpClient: HttpClient,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        httpClient.registerInterceptors(
            sessionTokenStorage = sessionTokenStorage,
            serviceTokenStorage = serviceTokenStorage,
        )
    }
}