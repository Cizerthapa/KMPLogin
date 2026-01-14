package com.example.loginkmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.loginkmp.data.local.SessionManager
import com.example.loginkmp.domain.repository.CartRepository
import com.example.loginkmp.domain.repository.ProductRepository
import com.example.loginkmp.presentation.screen.cart.CartScreen
import com.example.loginkmp.presentation.screen.login.LoginScreen
import com.example.loginkmp.presentation.screen.productdetail.ProductDetailScreen
import com.example.loginkmp.presentation.screen.productlist.ProductsScreen
import com.example.loginkmp.presentation.screen.profile.ProfileScreen
import com.example.loginkmp.presentation.theme.AppTheme
import com.example.loginkmp.presentation.viewmodel.CartViewModel
import com.example.loginkmp.presentation.viewmodel.ProductDetailViewModel
import com.example.loginkmp.presentation.viewmodel.ProductsViewModel
import com.example.loginkmp.presentation.viewmodel.ProfileViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(productRepository: ProductRepository, cartRepository: CartRepository) {
    var isDarkTheme by remember { mutableStateOf(false) } // Default to light or check system later if possible
    var showProfile by remember { mutableStateOf(false) }
    var showCart by remember { mutableStateOf(false) }

    AppTheme(darkTheme = isDarkTheme) {
        var isLoggedIn by remember { mutableStateOf(SessionManager.isLoggedIn()) }

        if (isLoggedIn) {
            when {
                showCart -> {
                    val cartViewModel = remember { CartViewModel(cartRepository) }
                    CartScreen(
                        state = cartViewModel.state,
                        onBack = { showCart = false }
                    )
                }
                showProfile -> {
                    // ProfileViewModel no longer needed for Cart, but if it has other logic keep it.
                    // For now, removing the need for it if it was only for Cart, or keep for potential future features.
                    // Assuming ProfileScreen logic doesn't strictly depend on ProfileViewModel anymore for this step
                    // or if it does, we need to adjust.
                    // Checking ProfileScreen signature... it doesn't take state anymore.
                    
                    ProfileScreen(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it },
                        onLogout = { isLoggedIn = false },
                        onBack = { showProfile = false },
                        onCartClick = { showCart = true }
                    )
                }

                else -> {
                    var selectedProductId by remember { mutableStateOf<Int?>(null) }

                    if (selectedProductId != null) {
                        val productDetailViewModel = remember {
                            ProductDetailViewModel(productRepository)
                        }
                        ProductDetailScreen(
                            productId = selectedProductId!!,
                            viewModel = productDetailViewModel,
                            onBack = { selectedProductId = null }
                        )
                    } else {
                        val viewModel = remember { ProductsViewModel(productRepository) }
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
