package at.fyayc.emporixapi.products

import at.fyayc.emporixapi.Configuration
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

@JsExport
class ProductApi(private val config: ApiConfiguration): IProductClient by ProductClient(Configuration(
    client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
            })

        }
    },
    endpoint = config.endpoint,
    tenant = config.tenant,
))