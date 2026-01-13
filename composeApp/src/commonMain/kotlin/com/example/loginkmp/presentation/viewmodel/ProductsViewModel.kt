package com.example.loginkmp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginkmp.domain.model.Product
import com.example.loginkmp.domain.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class ProductsViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    
    var state by mutableStateOf(ProductsState())
        private set
    
    private var fetchJob: Job? = null
    
    init {
        loadProducts()
    }
    
    fun loadProducts(page: Int = 1) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            if (page == 1) {
                state = state.copy(
                    isLoading = true,
                    error = null
                )
            } else {
                state = state.copy(isLoadingMore = true)
            }
            
            productRepository.getProducts(page = page, limit = ITEMS_PER_PAGE)
                .onSuccess { response ->
                    val newProducts = if (page == 1) {
                        response.products
                    } else {
                        state.products + response.products
                    }
                    
                    state = state.copy(
                        products = newProducts,
                        totalProducts = response.total,
                        currentPage = page,
                        hasMore = (page * ITEMS_PER_PAGE) < response.total,
                        isLoading = false,
                        isLoadingMore = false
                    )
                }
                .onFailure { error ->
                    // Offline Fallback for Page 1
                    if (page == 1) {
                        try {
                            // Try to get cached products
                            val cachedProducts = productRepository.products.first()
                            if (cachedProducts.isNotEmpty()) {
                                state = state.copy(
                                    products = cachedProducts,
                                    isLoading = false,
                                    isLoadingMore = false,
                                    error = "Offline Mode: Showing cached data"
                                )
                                return@launch
                            }
                        } catch (e: Exception) {
                            // Ignore cache failure
                        }
                    }
                    
                    state = state.copy(
                        error = error.message ?: "Failed to load products",
                        isLoading = false,
                        isLoadingMore = false
                    )
                }
        }
    }
    
    fun searchProducts(query: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null,
                searchQuery = query
            )
            
            productRepository.searchProducts(query)
                .onSuccess { response ->
                    state = state.copy(
                        products = response.products,
                        totalProducts = response.total,
                        currentPage = 1,
                        hasMore = false, // Reset for search
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    state = state.copy(
                        error = error.message ?: "Search failed",
                        isLoading = false
                    )
                }
        }
    }
    
    fun loadNextPage() {
        if (!state.isLoadingMore && state.hasMore) {
            loadProducts(state.currentPage + 1)
        }
    }
    
    fun refresh() {
        loadProducts(page = 1)
    }
    
    fun clearError() {
        state = state.copy(error = null)
    }
    
    companion object {
        private const val ITEMS_PER_PAGE = 20
    }
}

data class ProductsState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val totalProducts: Int = 0,
    val hasMore: Boolean = true,
    val searchQuery: String = ""
)
