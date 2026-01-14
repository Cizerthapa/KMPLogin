package com.example.loginkmp.data.repository

import com.example.loginkmp.data.remote.api.CartClient
import com.example.loginkmp.domain.model.CartResponse
import com.example.loginkmp.domain.repository.CartRepository

class CartRepositoryImpl : CartRepository {
    override suspend fun getCartsByUser(userId: Int): Result<CartResponse> {
        return CartClient.getCartsByUser(userId)
    }
}
