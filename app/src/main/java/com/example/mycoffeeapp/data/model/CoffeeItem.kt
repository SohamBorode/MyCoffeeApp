package com.example.mycoffeeapp.data.model

data class CoffeeItem(
    val id: String,
    val categoryId: String = "1",
    val name: String,
    val description: String,
    val price: Double,
    val currencySymbol: String = "₹",
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val isFavorite: Boolean = false,
    val size: String = "M",
    val temperature: String = "Hot",
    val longDescription: String,
    val ingredients: List<String>
)