package com.example.mycoffeeapp.data.remote.auth

import com.example.mycoffeeapp.data.mapper.toAppUser
import com.example.mycoffeeapp.data.model.auth.AppUser
import com.example.mycoffeeapp.data.model.auth.AuthSession
import com.example.mycoffeeapp.data.model.auth.LoginRequest
import com.example.mycoffeeapp.data.model.auth.SignupRequest
import java.util.UUID

data class DemoUser(
    val id: String,
    val name: String,
    val email: String?,
    val username: String?,
    val password: String
)

class DemoAuthDataSource : AuthDataSource {
    companion object {
        val demoUsers = mutableListOf(
            DemoUser(
                id = "user_1",
                name = "Demo User",
                email = "demo1@coffee.com",
                username = "demo1",
                password = "pass123"
            )
        )
        private var currentUserid: String? = null
    }

    override suspend fun login(loginRequest: LoginRequest): AuthSession {
        val identifier = loginRequest.email ?: loginRequest.username
        val user = demoUsers.firstOrNull {
            (it.email == identifier || it.username == identifier) && it.password == loginRequest.password
        } ?: throw IllegalArgumentException("Invalid credentials")

        currentUserid = user.id

        return AuthSession(appUser = user.toAppUser())

    }

    override suspend fun signup(signupRequest: SignupRequest): AuthSession {
        if (demoUsers.any { (it.email == signupRequest.email || it.username == signupRequest.username) && it.password == signupRequest.password }) {
            throw IllegalArgumentException("User Already exits, login or try to create new account")
        }
        val user = DemoUser(
            id = UUID.randomUUID().toString(),
            name = signupRequest.name,
            username = signupRequest.username,
            password = signupRequest.password,
            email = signupRequest.email
        )
        demoUsers.add(user)
        currentUserid = user.id
        return AuthSession(appUser = user.toAppUser())
    }

    override suspend fun getCurrentUser(): AppUser {
        return demoUsers.firstOrNull { it.id == currentUserid }?.toAppUser() 
            ?: throw IllegalStateException("No user currently logged in")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

}