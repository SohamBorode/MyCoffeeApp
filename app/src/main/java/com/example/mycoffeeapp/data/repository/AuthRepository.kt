package com.example.mycoffeeapp.data.repository

import com.example.mycoffeeapp.constants.Constants
import com.example.mycoffeeapp.data.model.auth.AppUser
import com.example.mycoffeeapp.data.model.auth.AuthSession
import com.example.mycoffeeapp.data.model.auth.LoginRequest
import com.example.mycoffeeapp.data.model.auth.SignupRequest
import com.example.mycoffeeapp.data.remote.auth.DemoAuthDataSource
import com.example.mycoffeeapp.data.remote.auth.RemoteAuthDataSource

class AuthRepository(
    private val remoteAuthDataSource: RemoteAuthDataSource,
    private val demoAuthDataSource: DemoAuthDataSource
) {
    suspend fun login(loginRequest: LoginRequest): AuthSession {
        return if (Constants.USE_BACKEND) {
                remoteAuthDataSource.login(loginRequest)
        } else {
            demoAuthDataSource.login(loginRequest)
        }
    }

    suspend fun signup(signupRequest: SignupRequest): AuthSession {
        return if (Constants.USE_BACKEND) {
                remoteAuthDataSource.signup(signupRequest)
        } else {
            demoAuthDataSource.signup(signupRequest)
        }
    }

    suspend fun getCurrentUser(): AppUser {
        return if (Constants.USE_BACKEND) {
                remoteAuthDataSource.getCurrentUser()
        } else {
            demoAuthDataSource.getCurrentUser()
        }
    }

    suspend fun logout(){
        if (Constants.USE_BACKEND)  remoteAuthDataSource.logout()
        else demoAuthDataSource.logout()
    }
}