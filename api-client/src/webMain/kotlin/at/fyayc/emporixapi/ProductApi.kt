package at.fyayc.emporixapi

import at.fyayc.emporixapi.products.IProductClient
import at.fyayc.emporixapi.products.ProductClient
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import js.objects.unsafeJso
import kotlinx.serialization.json.Json

@JsExport
class ProductApi(private val config: ApiConfiguration): IProductClient by ProductClient(
    Configuration(
        client = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    explicitNulls = false
                })

            }
        },
        endpoint = config.endpoint,
        tenant = config.tenant,
    )
)