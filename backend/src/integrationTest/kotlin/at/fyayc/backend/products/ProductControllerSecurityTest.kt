package at.fyayc.backend.products

import at.fyayc.backend.TestcontainersConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient


// example security test
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration::class)
class ProductControllerSecurityTest(
    @Autowired private val restTestClient: RestTestClient,
) {
    @Test
    fun name() {
        restTestClient.get().uri("/products/2")
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isForbidden
    }
}