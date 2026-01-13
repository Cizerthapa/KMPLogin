package com.example.loginkmp.domain.repository

import com.example.loginkmp.domain.model.Product
import com.example.loginkmp.domain.model.ProductsResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    val products: Flow<List<Product>>
    

    suspend fun getProducts(page: Int = 1, limit: Int = 20): Result<ProductsResponse>
    suspend fun searchProducts(query: String): Result<ProductsResponse>
    suspend fun getProduct(id: Int): Result<Product>
}
