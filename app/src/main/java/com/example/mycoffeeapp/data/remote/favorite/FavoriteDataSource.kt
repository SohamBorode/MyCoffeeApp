package com.example.mycoffeeapp.data.remote.favorite

import com.example.mycoffeeapp.data.model.cart.CartItem

interface FavoriteDataSource {
    suspend fun getFavoriteIds(): Set<String>
    suspend fun addFavorite(coffeeId: String)
    suspend fun removeFavorite(coffeeId: String)
}