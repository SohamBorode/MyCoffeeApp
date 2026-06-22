package com.example.mycoffeeapp.data.remote.profile

import com.example.mycoffeeapp.data.model.dto.ProfileDto

interface ProfileDataSource {
    suspend fun getProfileData(): ProfileDto

    suspend fun updateProfileData(profile: ProfileDto)
}
