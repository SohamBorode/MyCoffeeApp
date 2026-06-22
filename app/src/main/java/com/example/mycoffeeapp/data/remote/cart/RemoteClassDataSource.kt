package com.example.mycoffeeapp.data.remote.cart

import com.example.mycoffeeapp.data.mapper.toDomainModel
import com.example.mycoffeeapp.data.mapper.toDto
import com.example.mycoffeeapp.data.model.cart.CartItem

class RemoteClassDataSource(private val api : CartApiService) : CartDataSource {
    override suspend fun getCartItems(): List<CartItem> {
        return api.getCartItem().map { it.toDomainModel() }
    }

    override suspend fun addToCart(cartItem: CartItem) {
        api.addToCart(cartItem.toDto())
    }

    override suspend fun updateCartItem(cartItem: CartItem) {
        api.updateCartItem(cartItem.toDto())
    }

    override suspend fun removeFromCart(cartItemId: String) {
        api.deleteFromCart(cartItemId)
    }

}
