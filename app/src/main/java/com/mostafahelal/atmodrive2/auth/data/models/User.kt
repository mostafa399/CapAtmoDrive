package com.mostafahelal.atmodrive2.auth.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("birthday")
    val birthday: Any?,
    @SerializedName("captain_code")
    val captainCode: String?,
    @SerializedName("email")
    val email: Any?,
    @SerializedName("full_name")
    val fullName: Any?,
    @SerializedName("gender")
    val gender: Any?,
    @SerializedName("is_active")
    val isActive: Int?,
    @SerializedName("is_dark_mode")
    val isDarkMode: Int?,
    @SerializedName("lang")
    val lang: String?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("nationality")
    val nationality: Any?,
    @SerializedName("options")
    val options: Options?,
    @SerializedName("register_step")
    val registerStep: Int?,
    @SerializedName("remember_token")
    val rememberToken: String?,
    @SerializedName("status")
    val status: Int?
)
