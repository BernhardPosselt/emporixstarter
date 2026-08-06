package at.fyayc.emporixapi.oe

import at.fyayc.emporixapi.http.HmacSignature
import at.fyayc.emporixapi.http.withHmac
import at.fyayc.emporixapi.oe.events.OrchestrationEngineEvent
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class OrchestrationEngineEventClient(
    val secret: String,
    val client: HttpClient,
    val baseUrl: String,
    val source: String,
) {
    suspend fun <T : Any> publish(event: OrchestrationEngineEvent<T>): HttpResponse {
        return client.post(baseUrl) {
            accept(ContentType.Any)
            contentType(ContentType.Application.Json)
            header("ce-source", source)
            header("ce-id", event.id)
            header("ce-type", event.type)
            header("ce-specversion", "1.0")
            setBody(event.body, event.typeInfo)
            withHmac(HmacSignature(secret, "x-emporix-hmac"))
        }
    }
}