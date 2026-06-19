package com.example.mycoffeeapp.data.remote.favorite

import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.model.dto.CoffeeItemDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteApiService {
    @GET("favorite")
    suspend fun getFavoriteIds(): List<String>

    @POST("favorite/{coffeeId}")
    suspend fun addFavorite(@Path("coffeeId") coffeeId: String)

    @DELETE("favorite/{coffeeId}")
    suspend fun deleteFavoriteItem(@Path("coffeeId") coffeeId: String)
}