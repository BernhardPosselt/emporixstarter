package at.fyayc.emporixapi.wrappers.oe

import at.fyayc.emporixapi.oe.OrchestrationEngineEventClient
import at.fyayc.emporixapi.wrappers.EmporixHttpClient
import at.fyayc.emporixapi.wrappers.UnhandledResponseCode
import at.fyayc.emporixapi.wrappers.oe.events.OEEvent
import at.fyayc.emporixapi.wrappers.oe.events.SerializableEvent
import io.ktor.client.call.*

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

    suspend fun <E, T : Any, K : Any> publish(event: E): OEResponse
            where E : OEEvent<T>,
                  E : SerializableEvent<K> {
        val result = client.publish(event.toKt())
        return when (val code = result.status.value) {
            200 -> OEResponse.OEOkResponse(code, result.body())
            400 -> OEResponse.OEBadRequestResponse()
            401 -> OEResponse.OEForbiddenResponse()
            else -> throw UnhandledResponseCode(code)
        }
    }
}