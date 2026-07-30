package at.fyayc.emporixapi.products

import io.ktor.client.*
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.http.headers


class ProductClient(
    private val client: HttpClient,
    private val endpoint: String,
    private val tenant: String,
) {
    suspend fun createProduct(
        product: CreateProduct,
        skipVariantGeneration: Boolean,
        doIndex: Boolean,
        contentLanguage: String?,
    ) {
        val result = client.post(endpoint) {
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
                setBody(product)
            }
        }
    }
}