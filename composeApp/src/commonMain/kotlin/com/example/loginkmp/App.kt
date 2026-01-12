package com.example.loginkmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
@Preview
fun App() {
    MaterialTheme {
        var isLoggedIn by remember { mutableStateOf(false) }

        if (isLoggedIn) {
            var selectedProductId by remember { mutableStateOf<Int?>(null) }

            if (selectedProductId != null) {
                ProductDetailScreen(
                    productId = selectedProductId!!,
                    onBack = { selectedProductId = null }
                )
            } else {
                ProductListScreen(
                    onProductClick = { productId ->
                        selectedProductId = productId
                    }
                )
            }
        } else {
            LoginScreen(
                onLoginSuccess = {
                    isLoggedIn = true
                }
            )
        }
    }
}
