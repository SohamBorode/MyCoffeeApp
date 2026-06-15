package com.example.mycoffeeapp.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

sealed interface CartUiState {
    object Loading : CartUiState
    data class Success(val cartCoffeeList: List<CartItem>) : CartUiState
    data class Error(val msg: String) : CartUiState
}


class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)

//    private val _uiState  = MutableStateFlow<CartUiState>(
//        CartUiState.Success(demoCartDataSource.getCartItems())
//    )

    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

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
                    _uiState.value = CartUiState.Success(cartItemList)
                }
                .onFailure { err ->
                    _uiState.value =
                        CartUiState.Error(err.localizedMessage ?: "Unknown network error")
                }
        }
    }

    fun deleteItem(id : String){
        viewModelScope.launch {
            cartRepository.deleteCartItem(id)

            _uiState.value = CartUiState.Loading
            runCatching {
                cartRepository.getCartItems()
            }
                .onSuccess { cartItemsList ->
                    _uiState.value = CartUiState.Success(cartItemsList)
                }
                .onFailure {
                    err ->
                    _uiState.value = CartUiState.Error(err.localizedMessage?: "Error")
                }
        }
    }
}