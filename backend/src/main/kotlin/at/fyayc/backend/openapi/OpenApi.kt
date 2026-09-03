package at.fyayc.backend.openapi

import org.springdoc.core.customizers.OpenApiCustomizer

fun openApi(block: OpenApiConfigurer.() -> Unit) = OpenApiCustomizer {
    val configurer = OpenApiConfigurer(it)
    configurer.block()
}




