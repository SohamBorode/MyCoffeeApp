package com.example.mycoffeeapp.data.remote.auth

import com.example.mycoffeeapp.data.model.auth.AppUser
import com.example.mycoffeeapp.data.model.auth.AuthSession
import com.example.mycoffeeapp.data.model.auth.LoginRequest
import com.example.mycoffeeapp.data.model.auth.SignupRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): AuthSession

    @POST("auth/signup")
    fun signup(@Body signupRequest: SignupRequest): AuthSession

    @GET("auth/me")
    fun getCurrentUser(): AppUser

    @POST("auth/logout")
    fun logout()

}