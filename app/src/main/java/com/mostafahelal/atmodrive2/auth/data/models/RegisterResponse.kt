package com.mostafahelal.atmodrive2.auth.data.models

import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.auth.domain.model.RegisterResponseModel

data class RegisterResponse (
    @SerializedName("data")
    val data: User,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
){
   public fun asDomain(): RegisterResponseModel = RegisterResponseModel(
        data.asDomain(),message,status
    )
}