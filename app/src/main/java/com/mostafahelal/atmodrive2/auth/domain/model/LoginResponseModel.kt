package com.mostafahelal.atmodrive2.auth.domain.model

data class LoginResponseModel (
    val data: DataLogin?,
    val message: String,
    val status: Boolean
)