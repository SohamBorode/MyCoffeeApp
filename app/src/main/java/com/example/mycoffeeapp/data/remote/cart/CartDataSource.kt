package com.example.mycoffeeapp.data.remote.cart

import com.example.mycoffeeapp.data.model.cart.CartItem

interface CartDataSource {
    suspend fun getCartItems() : List<CartItem>
    suspend fun addToCart(cartItem: CartItem)
    suspend fun updateCartItem(cartItem: CartItem)
    suspend fun removeFromCart(cartItemId: String)
}