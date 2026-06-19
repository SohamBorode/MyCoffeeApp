package com.example.mycoffeeapp.data.remote.profile

interface ProfileDataSource {
    suspend fun getProfileData() : Set<String>

    suspend fun updateProfileData()
}