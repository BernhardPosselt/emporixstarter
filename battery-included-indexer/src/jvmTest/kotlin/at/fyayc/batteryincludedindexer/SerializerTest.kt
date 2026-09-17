package at.fyayc.batteryincludedindexer

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializerTest {
    @Test
    fun `should map test product`() {
        val data = this.javaClass.classLoader.getResourceAsStream("product.json")!!
        val json = Json {
            ignoreUnknownKeys = true
        }
        val result = json.decodeFromStream<SapArticle>(data)
        val expected = SapArticle(
            id = "catalog_rwa/Online/1792",

            )
        assertEquals(expected, result)
    }
}