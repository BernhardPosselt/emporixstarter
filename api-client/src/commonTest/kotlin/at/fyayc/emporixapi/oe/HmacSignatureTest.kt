package at.fyayc.emporixapi.oe.at.fyayc.emporixapi.oe

import at.fyayc.emporixapi.oe.hmacSignatureB64
import kotlinx.coroutines.test.runTest
import kotlin.io.encoding.Base64
import kotlin.test.Test
import kotlin.test.assertEquals

class HmacSignatureTest {
    @Test
    fun testHmac() = runTest {
        val actual = hmacSignatureB64("secret", "data")
        val expected = "GywWt1vSqHDBFBU8zaW8/KYzFLxyL6Fg1pDeEzzLuds="
        assertEquals(expected, Base64.encode(actual))
    }
}