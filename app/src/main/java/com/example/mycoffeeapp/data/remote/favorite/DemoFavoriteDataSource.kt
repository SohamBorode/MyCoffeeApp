package com.example.mycoffeeapp.data.remote.favorite

import com.example.mycoffeeapp.data.model.cart.CartItem

class DemoFavoriteDataSource : FavoriteDataSource {
    private val favoriteIds = mutableSetOf<String>()

    override suspend fun getFavoriteIds(): Set<String> = favoriteIds.toSet()

    override suspend fun addFavorite(coffeeId: String) {
        favoriteIds.add(coffeeId)
    }

    override suspend fun removeFavorite(coffeeId: String) {
        favoriteIds.remove(coffeeId)
    }
}