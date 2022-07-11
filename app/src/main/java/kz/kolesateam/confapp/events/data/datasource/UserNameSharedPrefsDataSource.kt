package kz.kolesateam.confapp.events.data.datasource

import android.content.SharedPreferences

private const val USER_NAME_KEY = "user_name"
private const val DEFAULT_USER_NAME = "Guest"

class UserNameSharedPrefsDataSource(
    private val sharedPreferences: SharedPreferences
): UserNameDataSource {

    override fun getSavedUserName(): String = sharedPreferences.getString(USER_NAME_KEY, DEFAULT_USER_NAME)
            ?: DEFAULT_USER_NAME

    override fun saveUserName(
        username: String
    ) {
        sharedPreferences.edit().putString(USER_NAME_KEY, username).apply()
    }
}