package at.fyayc.backend.openapi

import io.swagger.v3.oas.models.parameters.Parameter
import org.springframework.http.MediaType
import kotlin.reflect.KClass

@OpenApiDsl
class PathConfigurer<T : Any>(
    var id: String? = null,
    var tags: List<String> = emptyList(),
    var requestBody: KClass<T>? = null,
    var consumes: String = MediaType.APPLICATION_JSON_VALUE,
    var responses: Map<String, ResponseConfigurer<out Any>> = HashMap(),
    var description: String? = null,
    var required: Boolean = true,
    var parameters: List<Parameter> = emptyList(),
)