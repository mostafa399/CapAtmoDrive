package com.mostafahelal.atmodrive2.auth.domain.model

data class DataLogin(
    val is_new: Boolean,
    val user: UserLogin?,
    val full_name:String?
)
