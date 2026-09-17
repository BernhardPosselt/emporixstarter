package at.fyayc.batteryincludedindexer

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

suspend fun main() {
    val json = Json {
        explicitNulls = false
    }
    val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    val apiKey = System.getenv("BATTERY_INCLUDED_API_KEY")
    val config = ApiConfig(
        baseUrl = "https://api.batteryincluded.io",
        collection = "customer.testing.foryouandyourcustomers.com",
        apiKey = apiKey,
    )
    val indexer = Indexer(
        client = client,
        config = config,
        json = json,
    )
    indexer.indexMany(
        listOf(
            IndexedProduct(
                id = "1",
                cooperative = mapOf(
                    "1600" to Cooperative(
                        price = "19.11"
                    ),
                    "uvp" to Cooperative(
                        price = "18.11"
                    )
                ),
                name = "iphone"
            ),
            IndexedProduct(
                id = "2",
                cooperative = mapOf(
                    "1600" to Cooperative(
                        price = "15.11"
                    ),
                    "uvp" to Cooperative(
                        price = "181.11"
                    )
                ),
                name = "iphone 2"
            )
        ), typeOf<IndexedProduct>()
    )
}