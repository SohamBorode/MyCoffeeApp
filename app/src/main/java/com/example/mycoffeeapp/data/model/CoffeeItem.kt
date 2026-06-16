package com.example.mycoffeeapp.data.model

import java.time.temporal.TemporalQuery

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
    val size: String = "M",        // Default size for this item
    val temperature: String = "Hot" // Default temperature for this ite
)