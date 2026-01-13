package com.example.loginkmp.data.repository

import com.example.loginkmp.domain.model.Product
import com.example.loginkmp.data.local.dao.ProductDao
import com.example.loginkmp.domain.model.ProductsResponse
import com.example.loginkmp.data.local.entity.toDomain
import com.example.loginkmp.data.local.entity.toEntity
import com.example.loginkmp.domain.repository.ProductRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao,
    private val client: HttpClient
) : ProductRepository {
    // Single Source of Truth for Offline First (observes DB)
    override val products: Flow<List<Product>> = productDao.getAllProducts()
        .map { entities -> entities.map { it.toDomain() } }

    // Used for initial load / refresh / pagination
    override suspend fun getProducts(page: Int, limit: Int): Result<ProductsResponse> {
        return try {
            val skip = (page - 1) * limit
            val response = client.get("https://dummyjson.com/products") {
                parameter("limit", limit)
                parameter("skip", skip)
            }.body<ProductsResponse>()
            
            // Save to DB (Offline Cache)
            if (response.products.isNotEmpty()) {
                val entities = response.products.map { it.toEntity() }
                productDao.insertAll(entities)
            }
            
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    override suspend fun searchProducts(query: String): Result<ProductsResponse> {
         return try {
            val response = client.get("https://dummyjson.com/products/search") {
                parameter("q", query)
            }.body<ProductsResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProduct(id: Int): Result<Product> {
        // Try DB first
        val cached = productDao.getProductById(id)
        if (cached != null) {
            return Result.success(cached.toDomain())
        }
        // Fallback to Network
        return try {
             val response = client.get("https://dummyjson.com/products/$id").body<Product>()
             productDao.insert(response.toEntity())
             Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
