package com.example.mycoffeeapp.data.remote.profile

import com.example.mycoffeeapp.R

import javax.inject.Inject

data class User (
    val username : String = "Soham Borode",
    val profileImg : Int = R.drawable.coffee_5
)

class DemoProfileDataSource @Inject constructor() : ProfileDataSource {
    override suspend fun getProfileData(): Set<String> {
        // Instantiate User to access its properties
        val user = User()
        val username = user.username
        val profileImg = user.profileImg.toString() // Added parentheses
        return setOf(username, profileImg)
    }

    override suspend fun updateProfileData() {
        TODO("Not yet implemented")
    }
}