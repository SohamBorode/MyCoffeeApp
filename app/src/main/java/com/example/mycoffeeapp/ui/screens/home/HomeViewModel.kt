package com.example.mycoffeeapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.mapper.toDomainModel
import com.example.mycoffeeapp.data.repository.CoffeeRepository
import com.example.mycoffeeapp.data.repository.coffeeItemList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val coffeeList: List<CoffeeItem>) : HomeUiState
    data class Error(val msg : String) : HomeUiState
}

class HomeViewModel(private val repository: CoffeeRepository) : ViewModel() {
//    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    private val _uiState  = MutableStateFlow<HomeUiState>(
        HomeUiState.Success(com.example.mycoffeeapp.data.repository.coffeeItemList)
    )
    val uiState: StateFlow<HomeUiState> = _uiState

//    init {
//        loadCoffeeData()
//    }

    fun loadCoffeeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repository.fetchAllCoffees()
                .onSuccess { dtoList -> 
                    val domainList = dtoList.map { it.toDomainModel() }
                    _uiState.value = HomeUiState.Success(domainList) 
                }
                .onFailure { err -> 
                    _uiState.value = HomeUiState.Error(err.localizedMessage ?: "Unknown network error") 
                }
        }
    }
}