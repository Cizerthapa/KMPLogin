package com.example.loginkmp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginkmp.domain.repository.ProductRepository
import com.example.loginkmp.domain.model.Product
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    var state by mutableStateOf(ProductDetailState())
        private set

    private var fetchJob: Job? = null

    fun loadProduct(productId: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            
            productRepository.getProduct(productId)
                .onSuccess { product ->
                    state = state.copy(
                        product = product,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    state = state.copy(
                        error = error.message ?: "Failed to load product details",
                        isLoading = false
                    )
                }
        }
    }
}

data class ProductDetailState(
    val product: Product? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
