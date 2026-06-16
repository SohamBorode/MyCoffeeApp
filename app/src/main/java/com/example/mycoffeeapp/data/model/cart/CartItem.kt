package com.example.mycoffeeapp.data.model.cart

data class CartItem(
    val id: String,          // Unique ID for this cart entry
    val coffeeId: String,    // ID of the original coffee item
    val name: String,
    val imageUrl: String,
    val price: Double,
    val size: String,        // e.g., "S", "M", "L"
    val temperature: String, // e.g., "Hot", "Iced"
    val quantity: Int = 1,
    val subtitle : String = ""
)