package com.example.mycoffeeapp.ui.screens.loginsignp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.data.local.SessionManager
import com.example.mycoffeeapp.data.model.auth.AuthSession
import com.example.mycoffeeapp.data.model.auth.LoginRequest
import com.example.mycoffeeapp.data.model.auth.SignupRequest
import com.example.mycoffeeapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idel : com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState
    object Loading : com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState
    data class Success(val session: AuthSession) :
        com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState

    data class Error(val msg: String) : com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState
}


class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState>(
            _root_ide_package_.com.example.mycoffeeapp.ui.screens.loginsignp.AuthUiState.Idel
        )

    val uiState = _uiState.asStateFlow()

    init {
        loadAuthpage()
    }

    fun loadAuthpage() {
        viewModelScope.launch {
            _uiState.value =
                AuthUiState.Idel
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value =
                AuthUiState.Loading
            try {
                val request = LoginRequest(email = email, password = password)
                val session = authRepository.login(request)
                sessionManager.saveSession(session)
                _uiState.value =
                    AuthUiState.Success(
                        session
                    )
            } catch (e: Exception) {
                _uiState.value =
                    AuthUiState.Error(
                        e.message ?: "An unknown  error occurred"
                    )
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
                sessionManager.saveSession(session)
                _uiState.value =
                    AuthUiState.Success(
                        session
                    )
            } catch (e: Exception) {
                _uiState.value =
                    AuthUiState.Error(
                        e.message ?: "Unknown error, unable to signup"
                    )
            }
        }
    }

    fun logout() {
        sessionManager.logout()
        _uiState.value = AuthUiState.Idel

        viewModelScope.launch {
            runCatching {
                authRepository.logout()
            }
        }
    }

}
