package com.example.mycoffeeapp.data.remote.profile

import android.content.Context
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.data.model.dto.AccountDto
import com.example.mycoffeeapp.data.model.dto.OrderDto
import com.example.mycoffeeapp.data.model.dto.ProfileDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DemoProfileDataSource @Inject constructor(
    private val context: Context
) : ProfileDataSource {


    // making the shared prefence for surviing the updates on app closed
    private val sharedPrefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

    private var profile = ProfileDto(
        username = "Soham Borode",
        profileImageUri = null
    )

    override suspend fun getProfileData(): ProfileDto {
        // 2. Load the saved URI from the "Notebook" (Disk)
        val mySavedUri = sharedPrefs.getString("profile_image_path", null)
        // 3. Update the local variable so the app has the latest data
        profile = profile.copy(profileImageUri = mySavedUri)
        return profile
    }

    override suspend fun updateProfileData(profile: ProfileDto) {
        this.profile = profile

        val editor = sharedPrefs.edit()

        editor.putString("profile_image_path", profile.profileImageUri).apply()
    }

    override suspend fun getAccountDetails() : AccountDto{
        return AccountDto(
            username = profile.username,
            fullName = "Soham Nilesh Borode",
            email = "cksmcksmckmkd.com",
            dob = "18/09/1008",
            phonNo = "+91972xxxxx"
        )
    }

    override suspend fun getOrders(): List<OrderDto> {
        TODO("Not yet implemented")
    }
}
