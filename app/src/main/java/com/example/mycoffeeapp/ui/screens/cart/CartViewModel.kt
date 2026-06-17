package com.example.mycoffeeapp.ui.screens.cart

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.model.cart.CartItem
import com.example.mycoffeeapp.data.repository.cart.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

//new implementation part
data class PaymentMethod(
    val id : String,
    val title : String,
    @DrawableRes val iconRes: Int
)

val paymentOptions = listOf(
    PaymentMethod("netBanking", "Net Banking", R.drawable.net_banking),
    PaymentMethod("card", "Credit / Debit Card", R.drawable.debit_card),
    PaymentMethod("upi", "Upi Payment", R.drawable.upi),
    PaymentMethod("digiWallet", "Digital Wallet", R.drawable.digital_wallet),
    PaymentMethod("crypto", "Cryptocurrency", R.drawable.crypto),
    PaymentMethod("cashOnDelivery", "Cash On Delivery", R.drawable.cash_on_delivery),
    PaymentMethod("prepaid", "Prepaid / Gift Card", R.drawable.gift_card),
    PaymentMethod("bnpl", "Buy Now Pay Later", R.drawable.pay_later)
)
data class CartSummary(
    val subTotal: Double,
    val deliveryFee: Double,
    val total: Double,
)

sealed interface CartUiState {
    object Loading : CartUiState
    data class Success(
        val cartCoffeeList: List<CartItem>,
        val summary: CartSummary,

        val paymentMethod: List<PaymentMethod>,
        val selectedPaymentMethod: PaymentMethod,
        val isPaymentCardExpanded : Boolean
    ) : CartUiState
    data class Error(val msg: String) : CartUiState
}


class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

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
                    val previous = _uiState.value as? CartUiState.Success
                    _uiState.value = CartUiState.Success(
                        cartCoffeeList = cartItemList,
                        summary = calculateSummary(cartItemList),
                        paymentMethod = paymentOptions,
                        selectedPaymentMethod = previous?.selectedPaymentMethod?:paymentOptions.first(),
                        isPaymentCardExpanded = previous?.isPaymentCardExpanded?: false
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
//            loadCartData()
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
            total = subtotal + deliveryFee,
        )



    }

    fun togglePaymentMethod(){
        val current = _uiState.value as? CartUiState.Success ?: return
        _uiState.value = current.copy(isPaymentCardExpanded = !current.isPaymentCardExpanded)
    }

    fun selectPayment(method: PaymentMethod){
        val current = _uiState.value as? CartUiState.Success ?: return
        _uiState.value = current.copy(isPaymentCardExpanded = false, selectedPaymentMethod = method)
    }
}