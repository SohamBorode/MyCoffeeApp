package com.example.mycoffeeapp.data.remote.profile

import com.example.mycoffeeapp.data.model.dto.AccountDto
import com.example.mycoffeeapp.data.model.dto.OrderDto
import com.example.mycoffeeapp.data.model.dto.ProfileDto

interface ProfileDataSource {
    suspend fun getProfileData(): ProfileDto

    suspend fun updateProfileData(profile: ProfileDto)
    suspend fun getAccountDetails() : AccountDto
    suspend fun getOrders() : List<OrderDto>
}
