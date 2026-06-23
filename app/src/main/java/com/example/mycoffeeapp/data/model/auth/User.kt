package com.example.mycoffeeapp.data.model.auth

data class AppUser (
    val id: String,
    val name: String,
    val email: String?,
    val profileImgUrl: String? = null
)

data class LoginRequest(
    val email: String? = null,
    val username: String? = null,
    val password : String
)

data class SignupRequest(
    val name: String,
    val email: String?,
    val username: String?,
    val password: String
)

data class AuthSession(
    val appUser  :AppUser,
    val accessToken : String? = null
)