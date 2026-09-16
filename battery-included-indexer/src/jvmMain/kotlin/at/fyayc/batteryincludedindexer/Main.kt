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
        }
    }
    val config = ApiConfig(
        baseUrl = "https://api.batteryincluded.io",
        collection = "testing.foryouandyourcustomers.com",
        apiKey = System.getenv("BATTERY_INCLUDED_API_KEY"),
    )
    val indexer = Indexer(
        client = client,
        config = config,
        json = json,
    )
    indexer.indexMany(listOf(SapArticle(id = "1"), SapArticle(id = "2")), typeOf<SapArticle>())
}