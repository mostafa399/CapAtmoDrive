package com.mostafahelal.atmodrive2.auth.data.data_source.local

import android.content.SharedPreferences

interface ISharedPreferencesManager {
    val editor : SharedPreferences.Editor
    fun updateIsFirstTime(isFirstTime:Boolean)
    fun getIsFirstTime():Boolean
    fun  userIsLoggedIn() : Boolean
    fun getUserToken(): String
    fun saveUserAccessToken(token: String)
    fun deleteAccessToken(): Boolean
    fun saveString(key: String, value: String?)
    fun saveBoolean(key: String, value: Boolean)
    fun getString(key: String, defaultValue: String = ""): String
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun clearString(key: String)
}