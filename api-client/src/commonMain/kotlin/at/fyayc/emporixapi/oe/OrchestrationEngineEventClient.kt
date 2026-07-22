package at.fyayc.emporixapi.oe

import at.fyayc.emporixapi.HmacSignature
import at.fyayc.emporixapi.withHmac
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.setBody
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*

class OrchestrationEngineEventClient(
    val secret: String,
    val client: HttpClient,
    val eventEndpointUrl: String,
    val source: String,
) {
    suspend fun <T : Any> publish(event: OrchestrationEngineEvent<T>): HttpResponse {
        return client.post(eventEndpointUrl) {
            accept(ContentType.Any)
            contentType(ContentType.Application.Json)
            header("ce-source", source)
            header("ce-id", event.id)
            header("ce-type", event.type)
            header("ce-specversion", "1.0")
            setBody(event.payload, event.typeInfo)
            withHmac(HmacSignature(secret, "x-emporix-hmac"))
        }
    }
}