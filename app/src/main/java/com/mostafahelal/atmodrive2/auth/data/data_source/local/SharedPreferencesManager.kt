package com.mostafahelal.atmodrive2.auth.data.data_source.local

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(private val sharedPrefrences: SharedPreferences):
    ISharedPreferencesManager {
    companion object{
        const val SHARED_PREFERENCE_NAME="shared_pref"
        const val KEY_IS_FIRST_TIME="is_first_time"
        const val USER_IS_LOGGED_IN = "USER_IS_LOGGED_IN"


    }


    override val editor: SharedPreferences.Editor
        get() = sharedPrefrences.edit()

    override fun updateIsFirstTime(isFirstTime: Boolean) {
        with(editor){
            putBoolean(KEY_IS_FIRST_TIME,isFirstTime)
            apply()
        }
    }

    override fun getIsFirstTime(): Boolean {
        return sharedPrefrences.getBoolean(KEY_IS_FIRST_TIME,true)
    }

    override fun userIsLoggedIn(): Boolean {
        val token = sharedPrefrences.getString(USER_IS_LOGGED_IN, null)
        return token != null
    }

    override fun getUserToken(): String {
        return sharedPrefrences.getString(USER_IS_LOGGED_IN, "").toString()
    }

    override fun saveUserAccessToken(token: String) {
        sharedPrefrences.edit().putString(USER_IS_LOGGED_IN, token).apply()
    }

    override fun deleteAccessToken(): Boolean {
        sharedPrefrences.edit().remove(USER_IS_LOGGED_IN).apply()
        return userIsLoggedIn()
    }

    override fun saveString(key: String, value: String?) {
        sharedPrefrences.edit()
            .putString(key, value)
            .apply()
    }

    override fun getString(key: String, defaultValue: String): String {
        return sharedPrefrences.getString(key, defaultValue) ?: defaultValue
    }

    override fun clearString(key: String) {
        sharedPrefrences.edit()
            .remove(key)
            .apply()
    }


}