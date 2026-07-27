package at.fyayc.emporixapi.wrappers

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

@JsExport
class EmporixHttpClient {
    internal val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
            })
        }
    }
}