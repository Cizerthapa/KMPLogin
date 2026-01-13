package com.example.loginkmp.data.local

import com.example.loginkmp.data.remote.dto.LoginResponse
import com.russhwolf.settings.*  

object SessionManager {
    private val settings: Settings = com.russhwolf.settings.Settings()
    
    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_FIRST_NAME = "user_first_name"
    
    fun saveUserSession(response: LoginResponse) {
        settings.putString(KEY_AUTH_TOKEN, response.accessToken)
        response.username.let { settings.putString(KEY_USER_NAME, it) }
        response.firstName.let { settings.putString(KEY_USER_FIRST_NAME, it) }
    }
    
    fun clearSession() {
        settings.remove(KEY_AUTH_TOKEN)
        settings.remove(KEY_USER_NAME)
        settings.remove(KEY_USER_FIRST_NAME)
        // if you want to clear all settings
        settings.clear()
    }
    
    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }
    
    fun getUserName(): String? {
        return settings.getStringOrNull(KEY_USER_NAME)
    }
    
    fun getFirstName(): String? {
        return settings.getStringOrNull(KEY_USER_FIRST_NAME)
    }
    
    fun getToken(): String? {
        return settings.getStringOrNull(KEY_AUTH_TOKEN)
    }
}