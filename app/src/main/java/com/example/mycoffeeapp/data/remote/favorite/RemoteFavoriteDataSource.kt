package com.example.mycoffeeapp.data.remote.favorite

class RemoteFavoriteDataSource(
    private val favoriteApiService: FavoriteApiService
) : FavoriteDataSource {

    override suspend fun getFavoriteIds(): Set<String> {
        return favoriteApiService.getFavoriteIds().toSet()
    }

    override suspend fun addFavorite(coffeeId: String) {
        favoriteApiService.addFavorite(coffeeId)
    }

    override suspend fun removeFavorite(coffeeId: String) {
        favoriteApiService.deleteFavoriteItem(coffeeId)
    }
}
