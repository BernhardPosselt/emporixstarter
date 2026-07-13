package at.fyayc.emporixstarter.products

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

// example integration test
@SpringBootTest
class ProductServiceTest(
    @Autowired private val service: ProductService,
) {
    @Test
    fun name() {
        assertEquals("hi", service.doSomething())
    }
}