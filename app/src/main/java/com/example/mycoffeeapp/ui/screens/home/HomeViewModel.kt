package com.example.mycoffeeapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.mapper.toDomainModel
import com.example.mycoffeeapp.data.repository.CoffeeRepository
import com.example.mycoffeeapp.data.repository.favorite.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val coffeeList: List<CoffeeItem>) : HomeUiState
    data class Error(val msg: String) : HomeUiState
}

class HomeViewModel(
    private val repository: CoffeeRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _allCoffeeList = MutableStateFlow<List<CoffeeItem>>(emptyList())
    private val _searchResultList = MutableStateFlow<List<CoffeeItem>?>(null)
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow("1")
    val selectedCategoryId: StateFlow<String> = _selectedCategoryId.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadCoffeeData()
    }

    fun loadCoffeeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repository.fetchAllCoffees()
                .onSuccess { dtoList ->
                    val domainList = dtoList.map { it.toDomainModel() }
                    _allCoffeeList.value = domainList
                    _searchResultList.value = null
                    applyVisibleList()
                }
                .onFailure { err ->
                    _uiState.value =
                        HomeUiState.Error(err.localizedMessage ?: "Unknown network error")
                }
        }
    }

    fun filterByItem(categoryId: String) {
        _selectedCategoryId.value = categoryId
        applyVisibleList()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        searchJob?.cancel()

        if (query.isBlank()) {
            _searchResultList.value = null
            applyVisibleList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(300)
            searchCoffee(query)
        }
    }

    private suspend fun searchCoffee(query: String) {
        _uiState.value = HomeUiState.Loading
        repository.searchCoffees(query)
            .onSuccess { dtoList ->
                val domainList = dtoList.map { it.toDomainModel() }
                _searchResultList.value = domainList
                applyVisibleList()
            }
            .onFailure { err ->
                _uiState.value =
                    HomeUiState.Error(err.localizedMessage ?: "Search failed")
            }
    }

    private fun applyVisibleList() {
        val favoriteIds = favoriteRepository.favoriteIds.value

        val baseList = if (_searchQuery.value.isBlank()) {
            _allCoffeeList.value
        } else {
            _searchResultList.value ?: emptyList()
        }

        val filtered = if (_selectedCategoryId.value == "1") {
            baseList
        } else {
            baseList.filter { it.categoryId == _selectedCategoryId.value }
        }

        _uiState.value = HomeUiState.Success(
            filtered.map { it.copy(isFavorite = it.id in favoriteIds) }
        )
    }


    fun toggleFavorite(itemId: String) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(itemId)
            applyVisibleList()
        }
    }

    /*
    fun toggleFavorite(itemId: String) {

        _allCoffeeList.value = _allCoffeeList.value.map {
            if (it.id == itemId) it.copy(isFavorite = !it.isFavorite) else it
        }

        _searchResultList.value = _searchResultList.value?.map {
            if (it.id == itemId) it.copy(isFavorite = !it.isFavorite) else it
        }

        applyVisibleList()
    }
    */

}
