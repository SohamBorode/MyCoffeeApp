package com.example.mycoffeeapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.data.repository.profile.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProfileUiState {
    object Loading : ProfileUiState

    data class Success(
        val username: String,
        val profileImg: Int
    ) : ProfileUiState

    data class Error(val msg: String) : ProfileUiState
}

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            runCatching {
                profileRepository.getProfileData()
            }
                .onSuccess {profileData ->
                   _uiState.value =  ProfileUiState.Success(
                        username = profileData.elementAt(0),
                        profileImg = profileData.elementAt(1).toInt()
                    )

                }
                .onFailure {err ->
                    _uiState.value = ProfileUiState.Error( err.localizedMessage?: "Failed to load profile")
                }
        }
    }
}