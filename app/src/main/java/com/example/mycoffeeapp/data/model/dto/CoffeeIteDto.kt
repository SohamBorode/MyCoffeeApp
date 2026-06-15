package com.example.mycoffeeapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable // Using Kotlin Serialization
data class CoffeeItemDto(
    val id: String,                    // Unique ID from DB
    val name: String,                  // e.g., "Cappuccino"
    val categoryId: String,            // To link with the Filter tabs
    val description: String,           // Short description for card
    val longDescription: String? = null, // Detailed description for Details screen
    val price: Double,                 // Base price
    val imageUrl: String,              // URL from backend (replaces local R.drawable)
    val rating: Double,                // e.g., 4.8
    val reviewCount: Int,              // e.g., 230
    val ingredients: List<String> = emptyList() // e.g., ["Coffee", "Milk"]
)