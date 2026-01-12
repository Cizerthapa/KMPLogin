package com.example.loginkmp

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthClientTest {
    @Test
    fun testFetchProduct() = runTest {
        val result = AuthClient.getProduct(1)
        assertTrue(result.isSuccess, "Product fetch should be successful")
        val product = result.getOrNull()
        assertEquals("Essence Mascara Lash Princess", product?.title)
    }
}
