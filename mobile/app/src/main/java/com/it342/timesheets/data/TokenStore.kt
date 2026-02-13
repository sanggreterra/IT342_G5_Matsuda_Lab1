package com.it342.timesheets.data

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_NAME = "timesheets_prefs"
private const val KEY_TOKEN = "timesheet_token"
private const val KEY_USER_ID = "timesheet_user_id"
private const val KEY_USERNAME = "timesheet_username"
private const val KEY_EMAIL = "timesheet_email"

class TokenStore(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun saveAuth(token: String, user: UserResponse) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putInt(KEY_USER_ID, user.userId)
            .putString(KEY_USERNAME, user.username)
            .putString(KEY_EMAIL, user.email)
            .apply()
    }

    fun getStoredUser(): UserResponse? {
        val userId = prefs.getInt(KEY_USER_ID, -1)
        val username = prefs.getString(KEY_USERNAME, null) ?: return null
        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        if (userId < 0) return null
        return UserResponse(userId = userId, username = username, email = email)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
