package com.example.mycoffeeapp.ui.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.data.mapper.toDomainModel
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.repository.CoffeeRepository
import com.example.mycoffeeapp.data.repository.favorite.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

sealed interface FavoriteUiState {
    data object Loading : FavoriteUiState
    data object Empty : FavoriteUiState
    data class Success(val favoriteCoffeeList: List<CoffeeItem>) : FavoriteUiState
    data class Error(val msg: String) : FavoriteUiState
}

class FavoriteViewModel(
    private val coffeeRepository: CoffeeRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _allCoffeeList = MutableStateFlow<List<CoffeeItem>>(emptyList())
    private val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    init {
        loadCoffeeData()
        viewModelScope.launch {
            favoriteRepository.favoriteIds.collect {
                applyFavoriteList()
            }
        }
    }
    fun loadCoffeeData() {
        viewModelScope.launch {
            _uiState.value = FavoriteUiState.Loading
            coffeeRepository.fetchAllCoffees()
                .onSuccess { dtoList ->
                    _allCoffeeList.value = dtoList.map { it.toDomainModel() }
                    applyFavoriteList()
                }
                .onFailure { err ->
                    _uiState.value = FavoriteUiState.Error(err.localizedMessage ?: "Unknown error")
                }
        }
    }

    private fun applyFavoriteList() {
        val favorites = _allCoffeeList.value.filter { favoriteRepository.isFavorite(it.id) }
        _uiState.value = if (favorites.isEmpty()) {
            FavoriteUiState.Empty
        } else {
            FavoriteUiState.Success(favorites)
        }
    }

    fun toggleFavorite(coffeeId: String) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(coffeeId)
        }
    }

}