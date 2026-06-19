package com.example.mycoffeeapp.data.repository.profile

import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.remote.cart.DemoCartDataSource
import com.example.mycoffeeapp.data.remote.cart.RemoteClassDataSource
import com.example.mycoffeeapp.data.remote.profile.DemoProfileDataSource
import com.example.mycoffeeapp.data.remote.profile.ProfileDataSource
import com.example.mycoffeeapp.data.remote.profile.RemoteProfileDataSource
import javax.inject.Inject

class ProfileRepository (
    private val remote: RemoteProfileDataSource, private val demo: DemoProfileDataSource
) {
    suspend fun getProfileData(): Set<String> {
        return if(Constants.USE_BACKEND){
            remote.getProfileData()
        }else{
            demo.getProfileData()
        }
    }

    suspend fun updateProfileData() {
        if(Constants.USE_BACKEND){
            remote.updateProfileData()
        }else{
            demo.updateProfileData()
        }
    }
}