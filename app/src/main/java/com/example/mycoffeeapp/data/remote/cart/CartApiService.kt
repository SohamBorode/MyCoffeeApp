package com.example.mycoffeeapp.data.remote.cart

import com.example.mycoffeeapp.data.model.cart.CartItem
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApiService{

    @GET("/cart/items")
    suspend fun getCartItem() : List<CartItem>

    @POST("/cart/add")
    suspend fun addToCart(@Body item : CartItem)

    @DELETE("/cart/delete/{cartItemId}")
    suspend fun deleteFromCart(@Path("cartItemId") cartItemId : String)
    @POST("/cart/cartItem/update")
    suspend fun updateCartItem(cartItem: CartItem)
}