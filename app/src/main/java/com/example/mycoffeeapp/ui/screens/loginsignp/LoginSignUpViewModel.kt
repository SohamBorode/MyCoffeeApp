package com.example.mycoffeeapp.ui.screens.loginsignp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.data.model.auth.AuthSession
import com.example.mycoffeeapp.data.model.auth.LoginRequest
import com.example.mycoffeeapp.data.model.auth.SignupRequest
import com.example.mycoffeeapp.data.repository.AuthRepository
import com.example.mycoffeeapp.ui.screens.profile.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idel : AuthUiState
    object Loading : AuthUiState
    data class Success(val session: AuthSession) : AuthUiState
    data class Error(val msg: String) : AuthUiState
}


class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idel)

    val uiState = _uiState.asStateFlow()

    init {
        loadAuthpage()
    }

    fun loadAuthpage() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Idel
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val request = LoginRequest(email = email, password = password)
                val session = authRepository.login(request)
                _uiState.value = AuthUiState.Success(session)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "An unknown  error occurred")
            }
        }
    }

    fun signup(name: String, email: String, username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val request = SignupRequest(
                    name = name,
                    email = email,
                    username = username,
                    password = password
                )
                val session = authRepository.signup(request)
                _uiState.value = AuthUiState.Success(session)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Unknown error, unable to signup")
            }
        }
    }

    fun logout() {

    }
}