package com.example.mycoffeeapp.data.repository

import com.example.mycoffeeapp.data.model.CoffeeItem

val coffeeItemList = listOf(
    CoffeeItem(
        id = "1",
        name = "Cappuccino",
        description = "With Chocolate",
        price = 250.0,
        imageUrl = "https://images.unsplash.com/photo-1534778101976-62847782c213",
        rating = 4.8,
        reviewCount = 230,
        isFavorite = true
    ),
    CoffeeItem(
        id = "2",
        name = "Espresso",
        description = "Strong & Bold",
        price = 180.0,
        imageUrl = "https://images.unsplash.com/photo-1510591509098-f4fdc6d0ff04",
        rating = 4.7,
        reviewCount = 150
    ),
    CoffeeItem(
        id = "3",
        name = "Latte",
        description = "Rich & Creamy",
        price = 220.0,
        imageUrl = "https://images.unsplash.com/photo-1536939459926-301728717817",
        rating = 4.9,
        reviewCount = 310
    ),
    CoffeeItem(
        id = "4",
        name = "Mocha",
        description = "Chocolate flavor",
        price = 280.0,
        imageUrl = "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666",
        rating = 4.6,
        reviewCount = 180
    ),
    CoffeeItem(
        id = "5",
        name = "Flat White",
        description = "Smooth & Silky",
        price = 240.0,
        imageUrl = "https://images.unsplash.com/photo-1577968897966-3d4325b36b61",
        rating = 4.8,
        reviewCount = 200
    ),
    CoffeeItem(
        id = "6",
        name = "Americano",
        description = "Classic Black",
        price = 150.0,
        imageUrl = "https://images.unsplash.com/photo-1551033406-611cf9a28f67",
        rating = 4.5,
        reviewCount = 120
    ),
    CoffeeItem(
        id = "7",
        name = "Macchiato",
        description = "Caramel Drizzle",
        price = 300.0,
        imageUrl = "https://images.unsplash.com/photo-1485808191679-5f86510681a2",
        rating = 4.7,
        reviewCount = 140
    )
)