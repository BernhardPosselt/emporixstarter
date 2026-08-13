package at.fyayc.emporixapi.products

import at.fyayc.emporixapi.auth.ServiceToken
import at.fyayc.emporixapi.http.ApiConfig
import at.fyayc.emporixapi.http.parseOrThrow
import io.ktor.client.*
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.http.headers


class ProductClient(
    private val client: HttpClient,
    private val apiConfig: ApiConfig,
) {
    suspend fun createProduct(
        product: CreateProduct,
        skipVariantGeneration: Boolean,
        doIndex: Boolean,
        contentLanguage: String?,
        token: ServiceToken,
    ) {
        client.post(apiConfig.baseUrl) {
            url {
                appendPathSegments("product", apiConfig.tenant, "products")
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
                bearerAuth(token.accessToken)
                setBody(product)
            }
        }.parseOrThrow<Unit>()
    }
}

