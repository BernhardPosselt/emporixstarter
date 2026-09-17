package at.fyayc.backend

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.utility.DockerImageName
import java.io.File


@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    @Bean
    fun restTestClient(mockMvc: MockMvc): RestTestClient {
        return RestTestClient.bindTo(mockMvc).build()
    }

    @Bean
    fun redisContainer(): ComposeContainer =
        ComposeContainer(
            DockerImageName.parse("docker:25.0.5"),
            File("docker-compose-redis.yml")
        ).withExposedService("redis", 6379)
}
