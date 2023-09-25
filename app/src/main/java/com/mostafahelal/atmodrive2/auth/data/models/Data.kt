package com.mostafahelal.atmodrive2.auth.data.models


import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.auth.domain.model.DataLogin

data class Data(
    @SerializedName("is_new")
    val is_new: Boolean,
    @SerializedName("user")
    val user: User?,
    @SerializedName("full_name")
    val full_name:String?


    ){
    fun asDomain():DataLogin= DataLogin(is_new,user?.asDomain(),full_name)

}