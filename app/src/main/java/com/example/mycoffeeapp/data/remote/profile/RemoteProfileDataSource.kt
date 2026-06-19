package com.example.mycoffeeapp.data.remote.profile

import javax.inject.Inject

class RemoteProfileDataSource (private val profileApiService: ProfileApiService) : ProfileDataSource {

    override suspend fun getProfileData(): Set<String> {
        return profileApiService.getProfileData()
    }

    override suspend fun updateProfileData() {
        profileApiService.updateProfile()
    }
}