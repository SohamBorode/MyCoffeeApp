package com.example.mycoffeeapp.data.repository.profile

import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.mapper.toDomainModel
import com.example.mycoffeeapp.data.mapper.toDto
import com.example.mycoffeeapp.data.model.Account
import com.example.mycoffeeapp.data.model.Profile
import com.example.mycoffeeapp.data.model.dto.AccountDto
import com.example.mycoffeeapp.data.model.dto.OrderDto
import com.example.mycoffeeapp.data.remote.profile.ProfileDataSource

class ProfileRepository(
    private val remote: ProfileDataSource,
    private val demo: ProfileDataSource
) {
    suspend fun getProfileData(): Profile {
        return if (Constants.USE_BACKEND) {
            remote.getProfileData().toDomainModel()
        } else {
            demo.getProfileData().toDomainModel()
        }
    }

    suspend fun updateProfileData(profile: Profile) {
        if (Constants.USE_BACKEND) {
            remote.updateProfileData(profile.toDto())
        } else {
            demo.updateProfileData(profile.toDto())
        }
    }

    suspend fun getAccountDetails(): Account {
        val dto = if (Constants.USE_BACKEND) {
            remote.getAccountDetails()
        } else {
            demo.getAccountDetails()
        }
        return dto.toDomainModel()
    }

    suspend fun getOrders(): List<OrderDto> {
        val orders = if (Constants.USE_BACKEND){
            remote.getOrders()
        }else{
            demo.getOrders()
        }

        return  orders
    }
}
