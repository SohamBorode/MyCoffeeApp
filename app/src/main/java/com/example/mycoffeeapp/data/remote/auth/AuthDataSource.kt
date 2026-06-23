package com.example.mycoffeeapp.data.remote.auth

import com.example.mycoffeeapp.data.model.auth.AppUser
import com.example.mycoffeeapp.data.model.auth.AuthSession
import com.example.mycoffeeapp.data.model.auth.LoginRequest
import com.example.mycoffeeapp.data.model.auth.SignupRequest

interface AuthDataSource {
    suspend fun login(loginRequest: LoginRequest) : AuthSession
    suspend fun signup(signupRequest: SignupRequest)  : AuthSession
    suspend fun getCurrentUser() : AppUser
    suspend fun logout()
}