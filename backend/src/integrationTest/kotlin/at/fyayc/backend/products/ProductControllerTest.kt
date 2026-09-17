package at.fyayc.backend.products

import at.fyayc.backend.TestcontainersConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody

// example controller test
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
@WithMockUser(username = "admintest", roles = ["Admin"])
@Import(TestcontainersConfiguration::class)
class ProductControllerTest(
    @Autowired private val client: RestTestClient,
) {

    @Test
    fun name() {
        client.get().uri("/products/2")
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<Product>().value {
                assertEquals("2", it?.id)
            }
    }
}