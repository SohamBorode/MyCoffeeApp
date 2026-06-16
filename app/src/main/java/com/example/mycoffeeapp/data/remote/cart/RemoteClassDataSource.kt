package com.example.mycoffeeapp.data.remote.cart

import com.example.mycoffeeapp.data.model.cart.CartItem

class RemoteClassDataSource(private val api : CartApiService) : CartDataSource {
    override suspend fun getCartItems(): List<CartItem> {
        return api.getCartItem()
    }

    override suspend fun addToCart(cartItem: CartItem) {
        api.addToCart(cartItem)
    }

    override suspend fun updateCartItem(cartItem: CartItem) {
        api.updateCartItem(cartItem)
    }

    override suspend fun removeFromCart(cartItemId: String) {
        api.deleteFromCart(cartItemId)
    }

}