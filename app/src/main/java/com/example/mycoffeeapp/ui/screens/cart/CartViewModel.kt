package com.example.mycoffeeapp.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.mapper.toDomainModel
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.model.cart.CartItem
import com.example.mycoffeeapp.data.remote.cart.DemoCartDataSource
import com.example.mycoffeeapp.data.repository.CoffeeRepository
import com.example.mycoffeeapp.data.repository.cart.CartRepository
import com.example.mycoffeeapp.ui.screens.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

//new implementation part
data class CartSummary(
    val subTotal: Double,
    val deliveryFee: Double,
    val total: Double
)

sealed interface CartUiState {
    object Loading : CartUiState
    data class Success(val cartCoffeeList: List<CartItem>, val summary: CartSummary) : CartUiState
    data class Error(val msg: String) : CartUiState
}


class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)

    /*
    private val _uiState  = MutableStateFlow<CartUiState>(
        CartUiState.Success(demoCartDataSource.getCartItems())
    )
*/
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

//    val cartCount = (cartState as? CartUiState.Success)?.cartCoffeeList?.size ?: 0

    val cartCount: Int
        get() = (uiState.value as? CartUiState.Success)?.cartCoffeeList?.sumOf { it.quantity } ?: 0


    init {
        loadCartData()
    }

    fun loadCartData() { // Launch a coroutine to fetch data
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            runCatching {
                cartRepository.getCartItems()
            }
                .onSuccess { cartItemList ->
                    _uiState.value = CartUiState.Success(
                        cartCoffeeList = cartItemList, summary = calculateSummary(cartItemList),
                    )
                }
                .onFailure { err ->
                    _uiState.value =
                        CartUiState.Error(err.localizedMessage ?: "Unknown network error")
                }
        }
    }


    fun addToCart(
        coffeeItem: CoffeeItem,
        size: String,
        temperature: String,
        finalPrice: Double = coffeeItem.price,
    ) {
        viewModelScope.launch {
            runCatching {
                val currentItem = cartRepository.getCartItems()
                val existing = currentItem.firstOrNull {
                    it.coffeeId == coffeeItem.id && it.size == size && it.temperature == temperature
                }
                if (existing != null) {
                    cartRepository.updateCartItem(
                        existing.copy(
                            quantity = existing.quantity + 1,
                            price = finalPrice
                        )
                    )
                } else {
                    val cartItem = CartItem(
                        id = UUID.randomUUID().toString(),
                        coffeeId = coffeeItem.id,
                        name = coffeeItem.name,
                        imageUrl = coffeeItem.imageUrl,
                        price = finalPrice,
                        size = size,
                        temperature = temperature,
                        quantity = 1
                    )
                    cartRepository.addToCart(cartItem)
                }
            }
                .onSuccess {
                    loadCartData()
                }
                .onFailure { err ->
                    _uiState.value =
                        CartUiState.Error(err.localizedMessage ?: "Unable to update cart")
                }
            loadCartData()
        }
    }

    fun increaseQuantity(itemId: String) {
        changeQuantity(itemId, +1)
    }

    fun decreaseQuantity(itemId: String) {
        changeQuantity(itemId, -1)
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            runCatching {
                cartRepository.deleteCartItem(id)
            }.onSuccess {
                loadCartData()
            }.onFailure { err ->
                _uiState.value = CartUiState.Error(
                    err.localizedMessage ?: "Unable to delete item"
                )
            }
        }
    }

    private fun changeQuantity(itemId: String, delta: Int) {
        viewModelScope.launch {
            runCatching {
                val currentItems = cartRepository.getCartItems()
                val target = currentItems.firstOrNull { it.id == itemId } ?: return@runCatching

                val newQuantity = target.quantity + delta
                if (newQuantity > 0) {
                    cartRepository.updateCartItem(
                        target.copy(quantity = newQuantity)
                    )
                } else {
                    cartRepository.deleteCartItem(itemId)
                }
            }.onSuccess {
                loadCartData()
            }.onFailure { err ->
                _uiState.value = CartUiState.Error(
                    err.localizedMessage ?: "Unable to update quantity"
                )
            }
        }
    }

    private fun calculateSummary(items: List<CartItem>): CartSummary {
        val subtotal = items.sumOf { it.price * it.quantity }
        val deliveryFee = if (items.isNotEmpty()) 1.0 else 0.0
        return CartSummary(
            subTotal = subtotal,
            deliveryFee = deliveryFee,
            total = subtotal + deliveryFee
        )

    }
}