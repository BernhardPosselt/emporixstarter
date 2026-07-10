package at.fyayc.emporixstarter

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(Properties::class)
@SpringBootApplication
@SecurityScheme(
    name = "basicAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "basic"
)
@OpenAPIDefinition(
    info = Info(title = "Emporix Service API", version = "v1"),
)
class EmporixstarterApplication

fun main(args: Array<String>) {
    runApplication<EmporixstarterApplication>(*args)
}
