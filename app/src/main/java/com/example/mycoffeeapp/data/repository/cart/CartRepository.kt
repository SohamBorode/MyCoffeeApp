package com.example.mycoffeeapp.data.repository.cart

import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.model.cart.CartItem
import com.example.mycoffeeapp.data.remote.cart.CartDataSource

class CartRepository(
    private val remote: CartDataSource,
    private val demo: CartDataSource
) {
    suspend fun getCartItems(): List<CartItem> {
        if (!Constants.USE_BACKEND) return demo.getCartItems()

        return remote.getCartItems()
    }

    suspend fun deleteCartItem(cartItemId : String){
        if(Constants.USE_BACKEND){
            remote.removeFromCart(cartItemId)
        }else{
            demo.removeFromCart(cartItemId)
        }
    }

    suspend fun addToCart(cartItem: CartItem) {
        if (Constants.USE_BACKEND) {
            remote.addToCart(cartItem)
        } else {
            demo.addToCart(cartItem)
        }
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        if (Constants.USE_BACKEND){
            remote.updateCartItem(cartItem)
        }else{
            demo.updateCartItem(cartItem)
        }
    }


}
