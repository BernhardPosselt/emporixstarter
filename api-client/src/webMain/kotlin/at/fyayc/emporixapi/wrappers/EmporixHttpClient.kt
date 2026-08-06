package at.fyayc.emporixapi.wrappers

import at.fyayc.emporixapi.http.registerInterceptors
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
    }.also {
        it.registerInterceptors()
    }
}

