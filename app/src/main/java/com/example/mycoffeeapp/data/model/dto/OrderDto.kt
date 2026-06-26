package com.example.mycoffeeapp.data.model.dto

data class OrderDto(
    val orderId: String,
    val date: String,
    val totalAmount: Double,
    val status: String
)