package com.example.mycoffeeapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.data.model.CoffeeItem
import com.example.mycoffeeapp.data.mapper.toDomainModel
import com.example.mycoffeeapp.data.repository.CoffeeRepository
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

class HomeViewModel(private val repository: CoffeeRepository) : ViewModel() {
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

        _uiState.value = HomeUiState.Success(filtered)
    }

    fun toggleFavorite(itemId: String) {
        _allCoffeeList.value = _allCoffeeList.value.map {
            if (it.id == itemId) it.copy(isFavorite = !it.isFavorite) else it
        }

        _searchResultList.value = _searchResultList.value?.map {
            if (it.id == itemId) it.copy(isFavorite = !it.isFavorite) else it
        }

        applyVisibleList()
    }
}
