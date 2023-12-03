package com.mostafahelal.atmodrive2.auth.data.models

import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.auth.data.models.Options
import com.mostafahelal.atmodrive2.auth.domain.model.OptionsLogin
import com.mostafahelal.atmodrive2.auth.domain.model.UserLogin

data class User(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("captain_code")
    val captainCode: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("is_active")
    val isActive: Int?,
    @SerializedName("is_dark_mode")
    val isDarkMode: Int?,
    @SerializedName("lang")
    val lang: String?,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("nationality")
    val nationality: String?,
    @SerializedName("options")
    val options: Options?,
    @SerializedName("register_step")
    val registerStep: Int?,
    @SerializedName("remember_token")
    val rememberToken: String?,
    @SerializedName("status")
    val status: Int? ,
    @SerializedName("id")
    val id: Int?
){
    fun asDomain(): UserLogin = UserLogin(
        avatar,
        id,
        birthday,
        captainCode,
        email,
        fullName,
        gender,
        isActive,
        isDarkMode,
        lang,
        mobile,
        nationality,
        options?.asDomain(),
        registerStep,
        rememberToken,
        status
    )
}
