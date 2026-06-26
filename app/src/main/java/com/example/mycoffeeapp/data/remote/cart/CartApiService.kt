package com.example.mycoffeeapp.data.remote.cart

import com.example.mycoffeeapp.data.model.dto.CartItemDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApiService{

    @GET("api/cart/items")
    suspend fun getCartItem(): List<CartItemDto>

    @POST("api/cart/add")
    suspend fun addToCart(@Body item: CartItemDto)

    @DELETE("api/cart/delete/{cartItemId}")
    suspend fun deleteFromCart(@Path("cartItemId") cartItemId : String)

    @POST("api/cart/cartItem/update")
    suspend fun updateCartItem(@Body cartItem: CartItemDto)

    @POST("api/cart/clear")
    suspend fun clearCart()
}
