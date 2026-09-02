package at.fyayc.backend.openapi

import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.headers.Header
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import kotlin.reflect.KClass

/**
 * see https://github.com/springdoc/springdoc-openapi/blob/64d512824d8e01f8ec4d8fa3510a6ecd8d40aa57/springdoc-openapi-starter-common/src/main/java/org/springdoc/core/configuration/SpringDocSecurityConfiguration.java#L108
 * for how springdocs adds formLogin docs
 */
@OpenApiDsl
class OpenApiConfigurer(private val openAPI: OpenAPI) {
    private val converter = ModelConverters.getInstance(true)

    private fun <T : Any> resolveType(clazz: KClass<T>): Schema<*>? {
        val resolvedSchema = converter.resolveAsResolvedSchema(AnnotatedType(clazz.java))
        if (resolvedSchema.referencedSchemas != null && openAPI.components != null) {
            resolvedSchema.referencedSchemas.forEach { (key, schemasItem) ->
                openAPI.components.addSchemas(key, schemasItem)
            }
        }
        return resolvedSchema.schema
    }

    fun <T : Any> get(path: String, block: PathConfigurer<T>.() -> Unit) {
        val config = PathConfigurer<T>()
        config.block()
        operation(path, config, PathItem.HttpMethod.GET)
    }

    fun <T : Any> post(path: String, block: PathConfigurer<T>.() -> Unit) {
        val config = PathConfigurer<T>()
        config.block()
        operation(path, config, PathItem.HttpMethod.POST)
    }

    fun <T : Any> put(path: String, block: PathConfigurer<T>.() -> Unit) {
        val config = PathConfigurer<T>()
        config.block()
        operation(path, config, PathItem.HttpMethod.PUT)
    }

    fun <T : Any> delete(path: String, block: PathConfigurer<T>.() -> Unit) {
        val config = PathConfigurer<T>()
        config.block()
        operation(path, config, PathItem.HttpMethod.DELETE)
    }

    /**
     * behold the indentation! this is why we build a DSL on top of it
     */
    private fun <T : Any> operation(path: String, config: PathConfigurer<T>, method: PathItem.HttpMethod) {
        openAPI.path(
            path, PathItem()
                .apply {
                    operation(
                        method,
                        Operation()
                            .tags(config.tags)
                            .operationId(config.id)
                            .description(config.description)
                            .parameters(config.parameters)
                            .apply {
                                val body = config.requestBody
                                val appendToBody = if (body != null) {
                                    requestBody(
                                        RequestBody()
                                            .required(config.required)
                                            .content(
                                                Content().addMediaType(
                                                    config.consumes, MediaType()
                                                        .schema(resolveType(body))
                                                )
                                            )
                                    )
                                } else {
                                    requestBody(RequestBody())
                                }
                                appendToBody.responses(
                                    ApiResponses()
                                        .apply {
                                            config.responses.forEach { (code, response) ->
                                                addApiResponse(
                                                    code, ApiResponse()
                                                        .headers(response.headers.map { (name, value) ->
                                                            name to Header()
                                                                .description(value.description)
                                                                .required(value.required)
                                                        }.toMap())
                                                        .description(response.description)
                                                        .apply {
                                                            response.clazz?.let {
                                                                content(
                                                                    Content()
                                                                        .addMediaType(
                                                                            response.produces,
                                                                            MediaType()
                                                                                .schema(resolveType(it))
                                                                        )
                                                                )
                                                            }
                                                        }
                                                )
                                            }
                                        }
                                )
                            }
                    )
                }
        )
    }
}