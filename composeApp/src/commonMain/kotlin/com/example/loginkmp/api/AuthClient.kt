package com.example.loginkmp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object AuthClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response: LoginResponse = client.post("https://dummyjson.com/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProducts(): Result<ProductResponse> {
        return try {
            val response: ProductResponse = client.request("https://dummyjson.com/products").body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProduct(id: Int): Result<Product> {
        return try {
            val response: Product = client.request("https://dummyjson.com/products/$id").body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
