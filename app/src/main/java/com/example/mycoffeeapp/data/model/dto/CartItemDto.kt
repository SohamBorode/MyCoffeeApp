package com.example.mycoffeeapp.data.model.dto

data class CartItemDto(
    val id: String,
    val coffeeId: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val size: String,
    val temperature: String,
    val quantity: Int = 1,
    val subtitle: String = ""
)
