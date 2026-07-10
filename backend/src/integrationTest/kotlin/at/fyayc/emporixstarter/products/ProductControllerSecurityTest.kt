package at.fyayc.emporixstarter.products

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient


// example security test
@SpringBootTest
@AutoConfigureRestTestClient
@AutoConfigureMockMvc
class ProductControllerSecurityTest(
    @Autowired private val restTestClient: RestTestClient,
) {
    @Test
    fun name() {
        restTestClient.get().uri("/products/2")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isUnauthorized
    }
}