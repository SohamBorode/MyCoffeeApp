package com.example.mycoffeeapp.data.repository.favorite

import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.remote.cart.DemoCartDataSource
import com.example.mycoffeeapp.data.remote.cart.RemoteClassDataSource
import com.example.mycoffeeapp.data.remote.favorite.DemoFavoriteDataSource
import com.example.mycoffeeapp.data.remote.favorite.RemoteFavoriteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoriteRepository(
    private val remote: RemoteFavoriteDataSource, private val demo: DemoFavoriteDataSource
) {
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    suspend fun loadFavorites() {
        _favoriteIds.value = if (Constants.USE_BACKEND) {
            try {
                remote.getFavoriteIds()
            } catch (e: Exception) {
                demo.getFavoriteIds()
            }
        } else {
            demo.getFavoriteIds()
        }
    }

    suspend fun toggleFavorite(coffeeId: String) {
        val current = _favoriteIds.value
        if (coffeeId in current) {
            if (Constants.USE_BACKEND) {
                try { remote.removeFavorite(coffeeId) } catch (_: Exception) { demo.removeFavorite(coffeeId) }
            } else {
                demo.removeFavorite(coffeeId)
            }
        } else {
            if (Constants.USE_BACKEND) {
                try { remote.addFavorite(coffeeId) } catch (_: Exception) { demo.addFavorite(coffeeId) }
            } else {
                demo.addFavorite(coffeeId)
            }
        }
        loadFavorites()
    }

    fun isFavorite(coffeeId: String): Boolean = coffeeId in _favoriteIds.value

}