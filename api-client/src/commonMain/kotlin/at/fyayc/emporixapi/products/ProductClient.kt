package at.fyayc.emporixapi.products

import at.fyayc.emporixapi.Configuration
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.http.headers


class ProductClient(
    private val configuration: Configuration,
) : IProductClient {
    private val client = configuration.client
    private val tenant = configuration.tenant

    override suspend fun createProduct(
        product: CreateProduct,
        token: String,
        skipVariantGeneration: Boolean,
        doIndex: Boolean,
        contentLanguage: String?,
    ) {
        val result = client.post(configuration.endpoint) {
            url {
                appendPathSegments("product", tenant, "products")
                parameters {
                    if (doIndex) {
                        append("doIndex", "true")
                    }
                    if (skipVariantGeneration) {
                        append("skipVariantGeneration", "true")
                    }
                }
            }
            contentType(ContentType.Application.Json)
            headers {
                contentLanguage?.let {
                    append(HttpHeaders.ContentLanguage, it)
                }
                bearerAuth(token)
                setBody(product)
            }
        }
    }
}