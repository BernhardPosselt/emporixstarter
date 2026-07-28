package at.fyayc.emporixapi.wrappers.oe

import at.fyayc.emporixapi.oe.OrchestrationEngineEventClient
import at.fyayc.emporixapi.wrappers.EmporixHttpClient
import at.fyayc.emporixapi.wrappers.oe.events.OEEvent
import at.fyayc.emporixapi.wrappers.oe.events.SerializableEvent
import io.ktor.client.statement.*
import kotlinx.js.JsPlainObject

@JsPlainObject
@JsExport
external interface OEClientResponse {
    val code: Int
}

fun HttpResponse.toJs(): OEClientResponse {
    return OEClientResponse(
        code = this.status.value,
    )
}

@JsExport
class OEClient(
    httpClient: EmporixHttpClient,
    config: OEConfig
) {
    private val client = OrchestrationEngineEventClient(
        secret = config.secret,
        client = httpClient.client,
        baseUrl = config.baseUrl,
        source = config.source,
    )

    suspend fun <E, T : Any, K : Any> publish(event: E): OEClientResponse
            where E : OEEvent<T>,
                  E : SerializableEvent<K> {
        val result = client.publish(event.toKt())
        println(result.status.value)
        println(result.bodyAsText())
        return result.toJs()
    }
}