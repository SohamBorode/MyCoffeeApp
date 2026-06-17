package com.example.mycoffeeapp.data.repository

import androidx.compose.runtime.remember
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.data.model.CoffeeItem

val coffeeItemList = listOf(
    // --- Cappuccino (Category 2) ---
    CoffeeItem(
        "c1",
        "2",
        "Classic Cappuccino",
        "Balanced espresso & milk",
        250.0,
        imageUrl = "https://images.unsplash.com/photo-1534778101976-62847782c213",
        rating = 4.8,
        reviewCount = 230
    ),
    CoffeeItem(
        "c2",
        "2",
        "Chocolate Cappuccino",
        "With dark cocoa powder",
        280.0,
        imageUrl = "https://images.unsplash.com/photo-1572490122747-3968b75cc699",
        rating = 4.7,
        reviewCount = 150
    ),
    CoffeeItem(
        "c3",
        "2",
        "Vanilla Cappuccino",
        "Infused with vanilla bean",
        290.0,
        imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd",
        rating = 4.6,
        reviewCount = 90
    ),
    CoffeeItem(
        "c4",
        "2",
        "Caramel Cappuccino",
        "Drizzled with salted caramel",
        300.0,
        imageUrl = "https://images.unsplash.com/photo-1534778101976-62847782c213",
        rating = 4.9,
        reviewCount = 310
    ),
    CoffeeItem(
        "c5",
        "2",
        "Hazelnut Cappuccino",
        "Toasted hazelnut flavor",
        295.0,
        imageUrl = "https://images.unsplash.com/photo-1572490122747-3968b75cc699",
        rating = 4.5,
        reviewCount = 120
    ),
    CoffeeItem(
        "c6",
        "2",
        "Oatmilk Cappuccino",
        "Creamy dairy-free option",
        310.0,
        imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd",
        rating = 4.7,
        reviewCount = 140
    ),
    CoffeeItem(
        "c7",
        "2",
        "Dry Cappuccino",
        "Extra foam, less milk",
        240.0,
        imageUrl = "https://images.unsplash.com/photo-1534778101976-62847782c213",
        rating = 4.4,
        reviewCount = 65
    ),

    // --- Latte (Category 3) ---
    CoffeeItem(
        "l1",
        "3",
        "Caffè Latte",
        "Rich and creamy texture",
        220.0,
        imageUrl = "https://images.unsplash.com/photo-1536939459926-301728717817",
        rating = 4.9,
        reviewCount = 310
    ),
    CoffeeItem(
        "l2",
        "3",
        "Iced Latte",
        "Chilled with milk and ice",
        240.0,
        imageUrl = "https://images.unsplash.com/photo-1461023058943-07fcbe16d735",
        rating = 4.8,
        reviewCount = 200
    ),
    CoffeeItem(
        "l3",
        "3",
        "Pumpkin Spice Latte",
        "Autumn in a cup",
        320.0,
        imageUrl = "https://images.unsplash.com/photo-1542382257-80dedb725088",
        rating = 4.9,
        reviewCount = 450
    ),
    CoffeeItem(
        "l4",
        "3",
        "Matcha Latte",
        "Premium green tea base",
        350.0,
        imageUrl = "https://images.unsplash.com/photo-1536939459926-301728717817",
        rating = 4.7,
        reviewCount = 180
    ),
    CoffeeItem(
        "l5",
        "3",
        "Turmeric Latte",
        "Healthy golden milk",
        310.0,
        imageUrl = "https://images.unsplash.com/photo-1461023058943-07fcbe16d735",
        rating = 4.6,
        reviewCount = 110
    ),
    CoffeeItem(
        "l6",
        "3",
        "Honey Lavender Latte",
        "Floral and sweet",
        340.0,
        imageUrl = "https://images.unsplash.com/photo-1542382257-80dedb725088",
        rating = 4.8,
        reviewCount = 95
    ),
    CoffeeItem(
        "l7",
        "3",
        "Coconut Milk Latte",
        "Tropical vibes",
        330.0,
        imageUrl = "https://images.unsplash.com/photo-1536939459926-301728717817",
        rating = 4.5,
        reviewCount = 70
    ),

    // --- Americano (Category 4) ---
    CoffeeItem(
        "a1",
        "4",
        "Classic Americano",
        "Smooth black coffee",
        150.0,
        imageUrl = "https://images.unsplash.com/photo-1551033406-611cf9a28f67",
        rating = 4.5,
        reviewCount = 120
    ),
    CoffeeItem(
        "a2",
        "4",
        "Iced Americano",
        "Cold and refreshing",
        170.0,
        imageUrl = "https://images.unsplash.com/photo-1499961024600-ad094db305cc",
        rating = 4.6,
        reviewCount = 180
    ),
    CoffeeItem(
        "a3",
        "4",
        "Long Black",
        "Stronger aroma",
        180.0,
        imageUrl = "https://images.unsplash.com/photo-1521302080334-4bebac2763a6",
        rating = 4.7,
        reviewCount = 90
    ),
    CoffeeItem(
        "a4",
        "4",
        "Cold Brew",
        "Slow steeped for 12hrs",
        260.0,
        imageUrl = "https://images.unsplash.com/photo-1499961024600-ad094db305cc",
        rating = 4.9,
        reviewCount = 160
    ),
    CoffeeItem(
        "a5",
        "4",
        "Nitro Cold Brew",
        "Velvety smooth texture",
        350.0,
        imageUrl = "https://images.unsplash.com/photo-1551033406-611cf9a28f67",
        rating = 4.8,
        reviewCount = 210
    ),
    CoffeeItem(
        "a6",
        "4",
        "Red Eye",
        "Coffee with espresso shot",
        220.0,
        imageUrl = "https://images.unsplash.com/photo-1521302080334-4bebac2763a6",
        rating = 4.6,
        reviewCount = 85
    ),
    CoffeeItem(
        "a7",
        "4",
        "Decaf Americano",
        "All flavor, no caffeine",
        160.0,
        imageUrl = "https://images.unsplash.com/photo-1551033406-611cf9a28f67",
        rating = 4.4,
        reviewCount = 40
    ),

    // --- Espresso (Category 5) ---
    CoffeeItem(
        "e1",
        "5",
        "Single Espresso",
        "Strong and bold",
        120.0,
        imageUrl = "https://images.unsplash.com/photo-1510591509098-f4fdc6d0ff04",
        rating = 4.7,
        reviewCount = 150
    ),
    CoffeeItem(
        "e2",
        "5",
        "Double Espresso",
        "Extra caffeine kick",
        180.0,
        imageUrl = "https://images.unsplash.com/photo-1579992357154-faf4bfe95b3d",
        rating = 4.9,
        reviewCount = 220
    ),
    CoffeeItem(
        "e3",
        "5",
        "Espresso Con Panna",
        "With whipped cream",
        200.0,
        imageUrl = "https://images.unsplash.com/photo-1594631252845-29fc4cc8cde9",
        rating = 4.8,
        reviewCount = 130
    ),
    CoffeeItem(
        "e4",
        "5",
        "Affogato",
        "Over vanilla ice cream",
        290.0,
        imageUrl = "https://images.unsplash.com/photo-1594631252845-29fc4cc8cde9",
        rating = 4.9,
        reviewCount = 75
    ),
    CoffeeItem(
        "e5",
        "5",
        "Ristretto",
        "Short and concentrated",
        130.0,
        imageUrl = "https://images.unsplash.com/photo-1510591509098-f4fdc6d0ff04",
        rating = 4.6,
        reviewCount = 60
    ),
    CoffeeItem(
        "e6",
        "5",
        "Espresso Romano",
        "Served with lemon slice",
        140.0,
        imageUrl = "https://images.unsplash.com/photo-1579992357154-faf4bfe95b3d",
        rating = 4.3,
        reviewCount = 45
    ),
    CoffeeItem(
        "e7",
        "5",
        "Espresso Macchiato",
        "Marked with foam",
        160.0,
        imageUrl = "https://images.unsplash.com/photo-1510591509098-f4fdc6d0ff04",
        rating = 4.7,
        reviewCount = 115
    ),

    // --- Macchiato (Category 6) ---
    CoffeeItem(
        "m1",
        "6",
        "Caramel Macchiato",
        "Layered espresso & milk",
        300.0,
        imageUrl = "https://images.unsplash.com/photo-1485808191679-5f86510681a2",
        rating = 4.7,
        reviewCount = 140
    ),
    CoffeeItem(
        "m2",
        "6",
        "Latte Macchiato",
        "Mostly milk, stained by coffee",
        280.0,
        imageUrl = "https://images.unsplash.com/photo-1485808191679-5f86510681a2",
        rating = 4.6,
        reviewCount = 95
    ),
    CoffeeItem(
        "m3",
        "6",
        "Iced Caramel Macchiato",
        "Refreshing layered drink",
        320.0,
        imageUrl = "https://images.unsplash.com/photo-1485808191679-5f86510681a2",
        rating = 4.8,
        reviewCount = 210
    ),
    CoffeeItem(
        "m4",
        "6",
        "Hazelnut Macchiato",
        "Nutty and smooth",
        310.0,
        imageUrl = "https://images.unsplash.com/photo-1485808191679-5f86510681a2",
        rating = 4.5,
        reviewCount = 60
    ),
    CoffeeItem(
        "m5",
        "6",
        "Vanilla Macchiato",
        "Sweet vanilla base",
        290.0,
        imageUrl = "https://images.unsplash.com/photo-1485808191679-5f86510681a2",
        rating = 4.6,
        reviewCount = 80
    ),

    // --- Mocha (Category 7) ---
    CoffeeItem(
        "mo1",
        "7",
        "Caffè Mocha",
        "Espresso with chocolate",
        280.0,
        imageUrl = "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666",
        rating = 4.6,
        reviewCount = 180
    ),
    CoffeeItem(
        "mo2",
        "7",
        "White Mocha",
        "Sweet white chocolate",
        320.0,
        imageUrl = "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666",
        rating = 4.7,
        reviewCount = 120
    ),
    CoffeeItem(
        "mo3",
        "7",
        "Dark Chocolate Mocha",
        "Deep cocoa intensity",
        330.0,
        imageUrl = "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666",
        rating = 4.8,
        reviewCount = 90
    ),
    CoffeeItem(
        "mo4",
        "7",
        "Peppermint Mocha",
        "Cool and chocolatey",
        340.0,
        imageUrl = "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666",
        rating = 4.9,
        reviewCount = 200
    ),
    CoffeeItem(
        "mo5",
        "7",
        "Iced Mocha",
        "Blended chocolate coffee",
        300.0,
        imageUrl = "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666",
        rating = 4.6,
        reviewCount = 150
    ),

    // --- Flat White (Category 8) ---
    CoffeeItem(
        "fw1",
        "8",
        "Flat White",
        "Velvety microfoam",
        240.0,
        imageUrl = "https://images.unsplash.com/photo-1577968897966-3d4325b36b61",
        rating = 4.8,
        reviewCount = 200
    ),
    CoffeeItem(
        "fw2",
        "8",
        "Honey Flat White",
        "Sweet and silky",
        270.0,
        imageUrl = "https://images.unsplash.com/photo-1577968897966-3d4325b36b61",
        rating = 4.7,
        reviewCount = 110
    ),
    CoffeeItem(
        "fw3",
        "8",
        "Oat Flat White",
        "Nutty and smooth",
        290.0,
        imageUrl = "https://images.unsplash.com/photo-1577968897966-3d4325b36b61",
        rating = 4.6,
        reviewCount = 85
    ),
    CoffeeItem(
        "fw4",
        "8",
        "Strong Flat White",
        "Triple shot base",
        300.0,
        imageUrl = "https://images.unsplash.com/photo-1577968897966-3d4325b36b61",
        rating = 4.9,
        reviewCount = 140
    ),
    CoffeeItem(
        "fw5",
        "8",
        "Vanilla Flat White",
        "Fragrant and creamy",
        280.0,
        imageUrl = "https://images.unsplash.com/photo-1577968897966-3d4325b36b61",
        rating = 4.7,
        reviewCount = 65
    )
)