package at.fyayc.batteryincludedindexer

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KType

class Indexer(
    private val client: HttpClient,
    private val config: ApiConfig,
    private val json: Json,
) {
    suspend fun fetch(): List<SapArticle> {
        // TODO: this is hard coded
        val url =
            "http://lcsrvrwa006.rwa-test.at:8983/solr/master_lhCommerceIndex_Product_default/select?indent=true&q.op=OR&q=*%3A*&start=0&rows=20"
        val response = client.get(url) {
            contentType(ContentType.Application.Json)
        }
        return response.body<List<SapArticle>>()
    }

    suspend fun <I : Any> indexOne(product: I, type: KType, mode: IndexMode = IndexMode.NonTransaction.PARTIAL) {
        indexMany(listOf(product), type, mode)
    }

    suspend fun <I : Any> indexMany(
        items: Iterable<I>,
        type: KType,
        mode: IndexMode = IndexMode.NonTransaction.PARTIAL
    ) {
        client.post(config.baseUrl) {
            url {
                appendPathSegments("api", "v1", "collections", config.collection, "documents", "import")
            }
            parameters {
                when (mode) {
                    IndexMode.NonTransaction.FULL -> {
                        append("full", "1")
                    }

                    is IndexMode.Transaction -> {
                        append("transactionId", mode.id)
                    }

                    IndexMode.NonTransaction.PARTIAL -> {}
                }
            }
            headers {
                append("X-BI-API-KEY", config.apiKey)
            }
            val serializer = serializer(type)
            val body = ChannelWriterContent(
                body = {
                    items.forEach {
                        val value = json.encodeToString(serializer, it)
                        writeStringUtf8(value)
                        writeStringUtf8("\n")
                    }
                },
                contentType = ContentType("application", "x-ndjson"),
            )
            setBody(body)
            contentType(ContentType.Application.Json)
        }
    }
}