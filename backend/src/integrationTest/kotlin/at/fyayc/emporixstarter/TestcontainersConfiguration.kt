package at.fyayc.emporixstarter

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.RestTestClient


@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    @Bean
    fun restTestClient(mockMvc: MockMvc): RestTestClient {
        return RestTestClient.bindTo(mockMvc).build()
    }
}
