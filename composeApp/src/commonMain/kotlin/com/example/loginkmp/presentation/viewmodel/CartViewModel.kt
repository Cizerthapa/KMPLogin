package com.example.loginkmp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginkmp.domain.model.Cart
import com.example.loginkmp.domain.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    var state by mutableStateOf(CartState())
        private set

    init {
        loadCarts()
    }

    private fun loadCarts() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            cartRepository.getCartsByUser(5).onSuccess { response ->
                state = state.copy(
                    carts = response.carts,
                    isLoading = false
                )
            }.onFailure { error ->
                state = state.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to load carts"
                )
            }
        }
    }
}

data class CartState(
    val carts: List<Cart> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
