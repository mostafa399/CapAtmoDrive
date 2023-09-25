package com.mostafahelal.atmodrive2.auth.data.models

import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.auth.domain.model.LoginResponseModel

data class LoginResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
){
   public fun asDomain():LoginResponseModel=
        LoginResponseModel(data.asDomain(),message,status)

}
