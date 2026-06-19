package com.example.mycoffeeapp.data.remote.profile

import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApiService {
    @GET("user/profile")
    suspend fun getProfileData(): Set<String>

    @POST("user/profile/update")
    suspend fun updateProfile()
}