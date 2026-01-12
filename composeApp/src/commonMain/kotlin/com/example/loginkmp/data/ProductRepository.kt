package com.example.loginkmp.data

import com.example.loginkmp.AuthClient
import com.example.loginkmp.Product
import com.example.loginkmp.models.AppDatabase
import com.example.loginkmp.models.toDomain
import com.example.loginkmp.models.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class ProductRepository(
    private val database: AppDatabase,
    private val api: AuthClient // Injecting AuthClient separately or using singleton
) {
    val products: Flow<List<Product>> = database.productDao().getAllProducts()
        .map { entities -> entities.map { it.toDomain() } }
        .onStart {
            // Optimistic refresh
            refreshProducts()
        }

    suspend fun refreshProducts(): Result<Unit> {
        return try {
            val result = api.getProducts()
            result.onSuccess { response ->
                val entities = response.products.map { it.toEntity() }
                database.productDao().insertAll(entities)
            }
            if (result.isSuccess) Result.success(Unit) else Result.failure(Exception("Fetch failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProduct(id: Int): Result<Product> {
        // Try DB first
        val cached = database.productDao().getProductById(id)
        if (cached != null) {
             return Result.success(cached.toDomain())
        }
        
        // Fallback to API if not in DB (or force refresh if needed strategy)
        val apiResult = api.getProduct(id)
        apiResult.onSuccess { product ->
            database.productDao().insert(product.toEntity())
        }
        return apiResult
    }
}
