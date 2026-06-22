package com.example.mycoffeeapp.data.repository.favorite

import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.remote.favorite.FavoriteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoriteRepository(
    private val remote: FavoriteDataSource,
    private val demo: FavoriteDataSource
) {
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    suspend fun loadFavorites() {
        _favoriteIds.value = if (Constants.USE_BACKEND) {
            remote.getFavoriteIds()
        } else {
            demo.getFavoriteIds()
        }
    }

    suspend fun toggleFavorite(coffeeId: String) {
        val current = _favoriteIds.value
        if (coffeeId in current) {
            if (Constants.USE_BACKEND) {
                remote.removeFavorite(coffeeId)
            } else {
                demo.removeFavorite(coffeeId)
            }
        } else {
            if (Constants.USE_BACKEND) {
                remote.addFavorite(coffeeId)
            } else {
                demo.addFavorite(coffeeId)
            }
        }
        loadFavorites()
    }

    fun isFavorite(coffeeId: String): Boolean = coffeeId in _favoriteIds.value

}
