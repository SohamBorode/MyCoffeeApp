package com.example.mycoffeeapp.data.remote.profile

import com.example.mycoffeeapp.data.model.dto.ProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApiService {
    @GET("user/profile")
    suspend fun getProfileData(): ProfileDto

    @POST("user/profile/update")
    suspend fun updateProfile(@Body profile: ProfileDto)
}
