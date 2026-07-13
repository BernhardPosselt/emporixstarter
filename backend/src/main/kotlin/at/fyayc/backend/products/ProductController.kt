package at.fyayc.backend.products

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/products")
@Tag(name = "Products")
class ProductController(
    private val productService: ProductService,
) {
    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation
    fun getById(@PathVariable id: String): ResponseEntity<Product> {
        return ResponseEntity.ok(Product(id))
    }
}