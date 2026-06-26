package com.example.mycoffeeapp.data.remote.profile

import com.example.mycoffeeapp.data.model.dto.AccountDto
import com.example.mycoffeeapp.data.model.dto.OrderDto
import com.example.mycoffeeapp.data.model.dto.ProfileDto

class RemoteProfileDataSource(private val profileApiService: ProfileApiService) : ProfileDataSource {

    override suspend fun getProfileData(): ProfileDto {
        return profileApiService.getProfileData()
    }

    override suspend fun updateProfileData(profile: ProfileDto) {
        profileApiService.updateProfile(profile)
    }

    override suspend fun getAccountDetails(): AccountDto {
        return profileApiService.getAccountDetails()
    }

    override suspend fun getOrders(): List<OrderDto> {
        return profileApiService.getOrders()
    }
}
