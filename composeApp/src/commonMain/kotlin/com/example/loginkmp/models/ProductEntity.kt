package com.example.loginkmp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val tags: String, // List<String> serialized
    val brand: String?,
    val sku: String,
    val weight: Int,
    val thumbnail: String?,
    val images: String, // List<String> serialized
    // Storing other complex objects as JSON strings for simplicity
    val dimensions: String, 
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val reviews: String, 
    val returnPolicy: String,
    val minimumOrderQuantity: Int,
    val meta: String
)

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = Json.encodeToString(tags),
        brand = brand,
        sku = sku,
        weight = weight,
        thumbnail = thumbnail,
        images = Json.encodeToString(images),
        dimensions = Json.encodeToString(dimensions),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = Json.encodeToString(reviews),
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        meta = Json.encodeToString(meta)
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = Json.decodeFromString(tags),
        brand = brand,
        sku = sku,
        weight = weight,
        thumbnail = thumbnail,
        images = Json.decodeFromString(images),
        dimensions = Json.decodeFromString(dimensions),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = Json.decodeFromString(reviews),
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        meta = Json.decodeFromString(meta)
    )
}
