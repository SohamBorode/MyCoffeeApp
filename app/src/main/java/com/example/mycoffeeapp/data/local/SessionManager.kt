package com.example.mycoffeeapp.data.local

import android.content.Context
import com.example.mycoffeeapp.data.model.auth.AuthSession

class SessionManager(context: Context) {
    private val pref =
        context.applicationContext.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveSession(session: AuthSession) {
        pref.edit()
            .putBoolean("isLoggedIn", true)
            .putString("user_Id", session.appUser.id)
            .putString("user_name", session.appUser.name)
            .putString("user_email", session.appUser.email)
            .putString("accessToken", session.accessToken)
            .apply()
    }


    fun getAccessToken(): String? {
        return pref.getString("accessToken", null)
    }
    fun getUserId() : String? {
        return pref.getString("user_Id", null)
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean("isLoggedIn", false)
    }

    fun logout() {
        pref.edit().clear().apply()
    }
}