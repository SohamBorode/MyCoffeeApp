package com.example.mycoffeeapp.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.core.app.Person
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.data.repository.profile.ProfileRepository
import com.example.mycoffeeapp.hardware.camera.Permission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed interface ProfileImageAction {
    data object Camera : ProfileImageAction
    data object Gallery : ProfileImageAction
    data object Remove : ProfileImageAction
    data class SetImage(val uri: String?) : ProfileImageAction
}

sealed interface ProfileUiState {
    object Loading : ProfileUiState

    data class Success(
        val username: String,
        val profileImageUri: String? = null,
        val defaultProfileImage: Int = R.drawable.coffee_5
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
                        profileImageUri = profileData.elementAt(1)
                    )

                }
                .onFailure {err ->
                    _uiState.value = ProfileUiState.Error( err.localizedMessage?: "Failed to load profile")
                }
        }
    }

    fun onProfileImageSelected(uri : String?){
        val current  = _uiState.value
        if(current is ProfileUiState.Success){
            _uiState.value = current.copy(profileImageUri =  uri)
        }
    }

    fun removeProfileImage(){
        val current = _uiState.value
        if(current is ProfileUiState.Success){
            _uiState.value = current.copy(profileImageUri = null)
        }
    }

    fun handleProfileImageAction(action: ProfileImageAction) {
        when (action) {
            is ProfileImageAction.SetImage -> onProfileImageSelected(action.uri)
        ProfileImageAction.Remove -> removeProfileImage()
        ProfileImageAction.Camera -> {/* camera open action */ }
        ProfileImageAction.Gallery -> {/* gallery open action */ }
        }
    }
}