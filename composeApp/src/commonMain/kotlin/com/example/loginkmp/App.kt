package com.example.loginkmp

import com.example.loginkmp.theme.AppTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.loginkmp.data.ProductRepository
import com.example.loginkmp.screen.ProfileScreen

@Composable
@Preview
fun App(repository: ProductRepository) {
    var isDarkTheme by remember { mutableStateOf(false) } // Default to light or check system later if possible
    var showProfile by remember { mutableStateOf(false) }

    AppTheme(darkTheme = isDarkTheme) {
        var isLoggedIn by remember { mutableStateOf(SessionManager.isLoggedIn()) }

        if (isLoggedIn) {
            when {
                showProfile -> {
                    ProfileScreen(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it },
                        onLogout = { isLoggedIn = false },
                        onBack = { showProfile = false }
                    )
                }
                else -> {
                    var selectedProductId by remember { mutableStateOf<Int?>(null) }

                    if (selectedProductId != null) {
                        ProductDetailScreen(
                            productId = selectedProductId!!,
                            repository = repository,
                            onBack = { selectedProductId = null }
                        )
                    } else {
                        val viewModel = remember { com.example.loginkmp.viewmodel.ProductsViewModel(repository) }
                        ProductsScreen(
                            viewModel = viewModel,
                            onProductClick = { productId ->
                                selectedProductId = productId
                            },
                            onProfileClick = { showProfile = true }
                        )
                    }
                }
            }
        } else {
            LoginScreen(
                onLoginSuccess = {
                    isLoggedIn = true
                    showProfile = false // Reset profile state on new login
                }
            )
        }
    }
}
