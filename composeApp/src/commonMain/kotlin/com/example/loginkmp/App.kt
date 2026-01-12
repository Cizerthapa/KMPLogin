package com.example.loginkmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.setValue
import com.example.loginkmp.data.ProductRepository


@Composable
@Preview
fun App(repository: ProductRepository) {
    MaterialTheme {
        var isLoggedIn by remember { mutableStateOf(SessionManager.isLoggedIn()) }

        if (isLoggedIn) {
            var selectedProductId by remember { mutableStateOf<Int?>(null) }

            if (selectedProductId != null) {
                ProductDetailScreen(
                    productId = selectedProductId!!,
                    repository = repository,
                    onBack = { selectedProductId = null }
                )
            } else {
                ProductListScreen(
                    repository = repository,
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
