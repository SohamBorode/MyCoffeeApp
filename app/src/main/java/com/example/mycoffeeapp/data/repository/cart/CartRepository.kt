package com.example.mycoffeeapp.data.repository.cart

import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.model.cart.CartItem
import com.example.mycoffeeapp.data.remote.cart.DemoCartDataSource
import com.example.mycoffeeapp.data.remote.cart.RemoteClassDataSource

class CartRepository(
    private val remote: RemoteClassDataSource, private val demo: DemoCartDataSource
) {
    suspend fun getCartItems(): List<CartItem> {
        // 1. Check Network/Config Toggle
        if (!Constants.USE_BACKEND) return demo.getCartItems()

        return try {
            // 2. Check Connection to Server (The Call)
            val remoteData = remote.getCartItems()

            // 3. Check isData (Is the list empty?)
            if (remoteData.isNotEmpty()) {
                remoteData // Show Remote Data
            } else {
                demo.getCartItems() // Show Demo Data because remote is empty
            }
        } catch (e: Exception) {
            // 4. Fallback if Network/Server fails
            demo.getCartItems()
        }
    }

    suspend fun deleteCartItem(cartItemId : String){
        if(Constants.USE_BACKEND){
            try {
                remote.removeFromCart(cartItemId)
            }catch (e: Exception){
                demo.removeFromCart(cartItemId)
            }
        }else{
            demo.removeFromCart(cartItemId)
        }
    }
}