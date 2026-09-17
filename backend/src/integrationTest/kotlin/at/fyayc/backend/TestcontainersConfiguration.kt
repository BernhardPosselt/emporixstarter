package at.fyayc.backend

import com.redis.testcontainers.RedisContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient
import org.testcontainers.utility.DockerImageName


@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    @Bean
    fun restTestClient(mockMvc: MockMvc): RestTestClient {
        return RestTestClient.bindTo(mockMvc).build()
    }

    @Bean
    @ServiceConnection
    fun redisContainer(): RedisContainer =
        RedisContainer(DockerImageName.parse("redis:8.10.0"))
}
