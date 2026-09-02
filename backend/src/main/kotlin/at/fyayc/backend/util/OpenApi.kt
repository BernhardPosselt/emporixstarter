package at.fyayc.backend.util

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.utils.SpringDocAnnotationsUtils
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * Note that this method does not respect Jackson's @JsonProperty or Kotlin Serialization's @SerialName
 */
fun <T : Any> getRequiredFields(clazz: KClass<T>): Set<String> = clazz.memberProperties
    .filterNot { it.returnType.isMarkedNullable }
    .map { it.name }
    .toSet()


/**
 * see https://github.com/springdoc/springdoc-openapi/blob/64d512824d8e01f8ec4d8fa3510a6ecd8d40aa57/springdoc-openapi-starter-common/src/main/java/org/springdoc/core/configuration/SpringDocSecurityConfiguration.java#L108
 * for how springdocs adds formLogin docs
 */
fun <T : Any, O : Any> buildLoginDocs(
    path: String,
    method: PathItem.HttpMethod,
    id: String,
    description: String,
    requestBody: KClass<T>,
    responseBody: KClass<O>,
): OpenApiCustomizer = {
    val jsonMediaType = org.springframework.http.MediaType.APPLICATION_JSON_VALUE
    it.path(
        path, PathItem()
            .apply {
                operation(
                    method,
                    Operation()
                        .tags(listOf("Login"))
                        .operationId(id)
                        .requestBody(
                            RequestBody()
                                .required(true)
                                .content(
                                    Content()
                                        .addMediaType(
                                            jsonMediaType, MediaType()
                                                .schema(
                                                    SpringDocAnnotationsUtils.resolveSchemaFromType(
                                                        requestBody.java,
                                                        it.components,
                                                        null,
                                                    ).required(getRequiredFields(requestBody).toList())
                                                )
                                        )
                                )
                        )
                        .responses(
                            ApiResponses()
                                .addApiResponse(
                                    "200", ApiResponse()
                                        .description(description)
                                        .content(
                                            Content()
                                                .addMediaType(
                                                    jsonMediaType,
                                                    MediaType()
                                                        .schema(
                                                            SpringDocAnnotationsUtils.resolveSchemaFromType(
                                                                responseBody.java,
                                                                it.components,
                                                                null,
                                                            ).required(getRequiredFields(responseBody).toList())
                                                        )
                                                )
                                        )
                                )
                                .addApiResponse(
                                    "403", ApiResponse()
                                        .description("If anything fails")
                                )
                        )
                )
            }
    )
}
