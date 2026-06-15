package com.example.mycoffeeapp.data.model

data class CoffeeItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val currencySymbol: String = "₹",
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val isFavorite: Boolean = false
)