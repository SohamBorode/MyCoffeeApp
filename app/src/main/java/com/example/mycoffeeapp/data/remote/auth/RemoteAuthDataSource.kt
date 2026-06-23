package com.example.mycoffeeapp.data.remote.auth

import com.example.mycoffeeapp.data.model.auth.AppUser
import com.example.mycoffeeapp.data.model.auth.AuthSession
import com.example.mycoffeeapp.data.model.auth.LoginRequest
import com.example.mycoffeeapp.data.model.auth.SignupRequest

class RemoteAuthDataSource(private val authApiService: AuthApiService) : AuthDataSource{
    override suspend fun login(loginRequest: LoginRequest): AuthSession {
        return authApiService.login(loginRequest)
    }

    override suspend fun signup(signupRequest: SignupRequest): AuthSession {
        return authApiService.signup(signupRequest)
    }

    override suspend fun getCurrentUser(): AppUser {
     return  authApiService.getCurrentUser()
    }

    override suspend fun logout() {
        authApiService.logout()
    }

}