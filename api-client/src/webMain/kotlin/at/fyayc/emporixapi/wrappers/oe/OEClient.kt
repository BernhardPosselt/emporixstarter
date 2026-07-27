package at.fyayc.emporixapi.wrappers.oe

import at.fyayc.emporixapi.oe.OrchestrationEngineEventClient
import at.fyayc.emporixapi.wrappers.EmporixHttpClient
import at.fyayc.emporixapi.wrappers.oe.events.HelloWorldEvent
import at.fyayc.emporixapi.wrappers.oe.events.OEEvent
import at.fyayc.emporixapi.wrappers.oe.events.OtherEventEvent
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
    val httpClient: EmporixHttpClient,
    val config: OEConfig
) {
    private val client = OrchestrationEngineEventClient(
        secret = config.secret,
        client = httpClient.client,
        baseUrl = config.baseUrl,
        source = config.source,
    )

    suspend fun publish(event: OEEvent): OEClientResponse {
        when (event) {
            is HelloWorldEvent,
            is OtherEventEvent -> {
                val result = client.publish(event.toKt())
                return result.toJs()
            }

            else -> throw IllegalArgumentException("Not a supported event")
        }
    }
}