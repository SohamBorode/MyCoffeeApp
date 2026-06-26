package com.example.mycoffeeapp.data.remote.favorite

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteApiService {
    @GET("api/users/favorite")
    suspend fun getFavoriteIds(): List<String>

    @POST("api/users/favorites/{coffeeId}")
    suspend fun addFavorite(@Path("coffeeId") coffeeId: String)

    @DELETE("api/users/favorites/{coffeeId}")
    suspend fun deleteFavoriteItem(@Path("coffeeId") coffeeId: String)
}
