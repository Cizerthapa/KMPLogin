package com.example.loginkmp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CartResponse(
    val carts: List<Cart>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

@Serializable
data class Cart(
    val id: Int,
    val products: List<CartProduct>,
    val total: Double,
    val discountedTotal: Double,
    val userId: Int,
    val totalProducts: Int,
    val totalQuantity: Int
)

@Serializable
data class CartProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val quantity: Int,
    val total: Double,
    val discountPercentage: Double,
    val discountedTotal: Double,
    val thumbnail: String
)
