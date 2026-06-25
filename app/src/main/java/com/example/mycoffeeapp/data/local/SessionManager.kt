package com.example.mycoffeeapp.data.local

import android.content.Context
import com.example.mycoffeeapp.data.model.auth.AuthSession

class SessionManager(context: Context) {
    private val pref =
        context.applicationContext.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveLogin(userId: AuthSession) {
        pref.edit()
            .putBoolean("isLoggedIn", true)
            .putString("userId", userId)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean("isLoggedIn", false)
    }

    fun logout() {
        pref.edit().clear().apply()
    }
}