package com.example.mycoffeeapp.data.remote.cart

import com.example.mycoffeeapp.data.model.dto.CartItemDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApiService{

    @GET("/cart/items")
    suspend fun getCartItem(): List<CartItemDto>

    @POST("/cart/add")
    suspend fun addToCart(@Body item: CartItemDto)

    @DELETE("/cart/delete/{cartItemId}")
    suspend fun deleteFromCart(@Path("cartItemId") cartItemId : String)

    @POST("/cart/cartItem/update")
    suspend fun updateCartItem(@Body cartItem: CartItemDto)
}
