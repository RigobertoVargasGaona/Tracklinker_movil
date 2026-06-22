package com.example.appinterface.helpers

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "auth_prefs", Context.MODE_PRIVATE
    )

    fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun clearTokens() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }
}