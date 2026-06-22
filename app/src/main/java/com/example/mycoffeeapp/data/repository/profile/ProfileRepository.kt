package com.example.mycoffeeapp.data.repository.profile

import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.mapper.toDomainModel
import com.example.mycoffeeapp.data.mapper.toDto
import com.example.mycoffeeapp.data.model.Profile
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
}
