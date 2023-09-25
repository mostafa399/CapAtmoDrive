package com.mostafahelal.atmodrive2.auth.domain.model

data class DataRegister(
    val avatar: String,
    val birthday: String?,
    val captain_code: String,
    val email: String?,
    val full_name: String?,
    val gender: String?,
    val is_active: Int,
    val is_dark_mode: Int,
    val lang: String,
    val mobile: String,
    val nationality: String?,
    val options: OptionsLogin,
    val register_step: Int,
    val remember_token: String,
    val status: Int
)
