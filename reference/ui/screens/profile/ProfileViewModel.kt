package com.example.mycoffeeapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.data.model.Profile
import com.example.mycoffeeapp.data.model.dto.OrderDto
import com.example.mycoffeeapp.data.repository.profile.ProfileRepository
import com.example.mycoffeeapp.ui.screens.cart.CartUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ProfileSheetType {
    ACCOUNT, ORDERS, TERMS, HELP
}

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

sealed interface AccountUiState {
    object Loading : AccountUiState

    data class Success(
        val username: String,
        val fullName: String,
        val email: String,
        val dob: String,
        val phonNo: String,
        val passwordReset: String? = null,
        val profileImageUri: String? = null,
        val defaultProfileImage: Int = R.drawable.coffee_5,
        val isLoggedIn: Boolean,
        val isLoggedOut: Boolean
    ) : AccountUiState

    data class Error(val msg: String) : AccountUiState
}

sealed interface OrdersUiState {
    object Loading : OrdersUiState
    data class Success(val orders: List<OrderDto>) : OrdersUiState
    data class Error(val msg: String) : OrdersUiState
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
                .onSuccess { profileData ->
                    _uiState.value = ProfileUiState.Success(
                        username = profileData.username,
                        profileImageUri = profileData.profileImageUri
                    )

                }
                .onFailure { err ->
                    _uiState.value = ProfileUiState.Error(
                        err.localizedMessage ?: "Failed to load profile"
                    )
                }
        }
    }

    fun onProfileImageSelected(uri: String?) {
        val current = _uiState.value
        if (current is ProfileUiState.Success) {
            _uiState.value = current.copy(profileImageUri = uri)
            saveProfile(current.copy(profileImageUri = uri))
        }
    }

    fun removeProfileImage() {
        val current = _uiState.value
        if (current is ProfileUiState.Success) {
            _uiState.value = current.copy(profileImageUri = null)
            saveProfile(current.copy(profileImageUri = null))
        }
    }

    fun handleProfileImageAction(action: ProfileImageAction) {
        when (action) {
            is ProfileImageAction.SetImage -> onProfileImageSelected(action.uri)
            ProfileImageAction.Remove -> removeProfileImage()
            ProfileImageAction.Camera -> {/* camera open action */
            }

            ProfileImageAction.Gallery -> {/* gallery open action */
            }
        }
    }

    private fun saveProfile(state: ProfileUiState.Success) {
        viewModelScope.launch {
            runCatching {
                profileRepository.updateProfileData(
                    Profile(
                        username = state.username,
                        profileImageUri = state.profileImageUri
                    )
                )
            }.onFailure { err ->
                _uiState.value = ProfileUiState.Error(
                    err.localizedMessage ?: "Failed to update profile"
                )
            }
        }
    }


    // Selectin which sheet to show
    private val _activeSheet = MutableStateFlow<ProfileSheetType?>(null)
    val activeSheet = _activeSheet.asStateFlow()


    fun showBottonSheet(type: ProfileSheetType?) {
        _activeSheet.value = type
        if (type == ProfileSheetType.ACCOUNT) {
            loadAccountDetails()
        }
    }

    // Bottom Sheet Visibility
    private val _showAccountSheet = MutableStateFlow(false)
    val showAccountSheet = _showAccountSheet.asStateFlow()

    // Bottom Sheet Data State
    private val _accountUiState = MutableStateFlow<AccountUiState>(AccountUiState.Loading)
    val accountUiState = _accountUiState.asStateFlow()


    private fun loadAccountDetails() {
        viewModelScope.launch {
            _accountUiState.value = AccountUiState.Loading
            runCatching {
                profileRepository.getAccountDetails()
            }
                .onSuccess { accountDetails ->
                    _accountUiState.value = AccountUiState.Success(
                        username = accountDetails.username,
                        fullName = accountDetails.fullName,
                        email = accountDetails.email,
                        dob = accountDetails.dob,
                        phonNo = accountDetails.phonNo,
                        isLoggedIn = true,
                        isLoggedOut = false
                    )
                }
                .onFailure { err ->
                    _accountUiState.value = AccountUiState.Error(
                        err.localizedMessage ?: "Unknown error occurred in loading account"
                    )
                }
        }
    }

    private fun loadTermsAndCondition() {

    }


    private val _orderUiState = MutableStateFlow<OrdersUiState>(OrdersUiState.Loading)
    val oderUiState = _orderUiState.asStateFlow()

    private fun loadOrders() {
        viewModelScope.launch {
            _orderUiState.value = OrdersUiState.Loading
            runCatching {
                profileRepository.getOrders()
            }.onSuccess { orders ->
                _orderUiState.value = OrdersUiState.Success(orders)
            }.onFailure { err ->
                _orderUiState.value =
                    OrdersUiState.Error(err.localizedMessage ?: "Failed to load orders")
            }
        }
    }

    private fun loadHelp() {}


}
