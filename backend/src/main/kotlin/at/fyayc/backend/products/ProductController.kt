package at.fyayc.backend.products

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/products")
@Tag(name = "Products")
class ProductController(
    private val productService: ProductService,
) {
    @GetMapping(
        "/{id}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Operation
    fun getById(@PathVariable id: String): ResponseEntity<Product> {
        return ResponseEntity.ok(Product(id))
    }


    data class Test(val id: String)

    @PostMapping(
        "/",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Operation
    fun test(@RequestBody test: Test): ResponseEntity<Test> {
        return ResponseEntity.ok(test)
    }
}

