package com.example.mycoffeeapp.data.remote.profile

import com.example.mycoffeeapp.data.model.dto.AccountDto
import com.example.mycoffeeapp.data.model.dto.OrderDto
import com.example.mycoffeeapp.data.model.dto.ProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApiService {
    @GET("api/user/profile")
    suspend fun getProfileData(): ProfileDto

    @POST("api/user/profile/update")
    suspend fun updateProfile(@Body profile: ProfileDto)

    @GET("api/user/account")
    suspend fun getAccountDetails(): AccountDto

    @GET("api/user/orders")
    suspend fun getOrders() : List<OrderDto>

}
