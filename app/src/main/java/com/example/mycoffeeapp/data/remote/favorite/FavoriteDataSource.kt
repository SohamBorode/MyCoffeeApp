package com.example.mycoffeeapp.data.remote.favorite

interface FavoriteDataSource {
    suspend fun getFavoriteIds(): Set<String>
    suspend fun addFavorite(coffeeId: String)
    suspend fun removeFavorite(coffeeId: String)
}
