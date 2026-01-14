package com.example.loginkmp.data.remote.api

import com.example.loginkmp.domain.model.CartResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object CartClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getCartsByUser(userId: Int): Result<CartResponse> {
        return try {
            val response: CartResponse =
                client.get("https://dummyjson.com/carts").body()
            print("getCartsByUser: Response - $response")
            Result.success(response)
        } catch (e: Exception) {
            print("getCartsByUser Exception $e")
            Result.failure(e)
        }
    }
}
