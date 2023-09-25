package com.mostafahelal.atmodrive2.auth.domain.model

data class RegisterResponseModel(
    val data: UserLogin?,
    val message: String,
    val status: Boolean
)
