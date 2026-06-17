package com.example.mycoffeeapp.data.remote.cart

import androidx.compose.ui.res.painterResource
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.model.cart.CartItem

class DemoCartDataSource : CartDataSource {

    companion object {
        private val demoItems = mutableListOf(
            CartItem(
                id = "aaa1",
                coffeeId = "coffeeCapa1",
                name = "Cappuccino",
                imageUrl = "",
                price = 122.4,
                size = "M",
                temperature = "HOT",
                quantity = 2
            ),
            CartItem(
                id = "aaa2",
                coffeeId = "coffeeCapa1",
                name = "Cappuccino",
                imageUrl = "",
                price = 122.4,
                size = "M",
                temperature = "HOT",
                quantity = 2
            ),
            CartItem(
                id = "aaa3",
                coffeeId = "coffeeCapa1",
                name = "Cappuccino",
                imageUrl = "",
                price = 122.4,
                size = "M",
                temperature = "HOT",
                quantity = 2
            ),
            CartItem(
                id = "aaa4",
                coffeeId = "coffeeCapa1",
                name = "Cappuccino",
                imageUrl = "",
                price = 122.4,
                size = "M",
                temperature = "HOT",
                quantity = 2
            )
        )
    }

    override suspend fun getCartItems(): List<CartItem> {
        return demoItems.toList()
    }

    override suspend fun addToCart(cartItem: CartItem) {
        demoItems.add(cartItem)
    }

    override suspend fun updateCartItem(cartItem: CartItem) {
        val index = demoItems.indexOfFirst { it.id == cartItem.id }
        if (index!=-1){
            demoItems[index] = cartItem
        }
    }

    override suspend fun removeFromCart(cartItemId: String) {
        demoItems.removeAll { it.id == cartItemId }
    }
}