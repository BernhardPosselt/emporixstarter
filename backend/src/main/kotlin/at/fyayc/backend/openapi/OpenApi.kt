package at.fyayc.backend.openapi

import org.springdoc.core.customizers.OpenApiCustomizer

fun x() {
    openApi {
        get("/login") {
            id = ""
            tags = listOf("tag1")
            requestBody = String::class
            responses = mapOf(
                "200" to response(String::class) {
                    description = ""
                }
            )
        }
    }
}

fun openApi(block: OpenApiConfigurer.() -> Unit) = OpenApiCustomizer {
    val configurer = OpenApiConfigurer(it)
    configurer.block()
}




