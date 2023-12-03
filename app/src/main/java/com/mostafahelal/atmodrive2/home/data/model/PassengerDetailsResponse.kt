package com.mostafahelal.atmodrive2.home.data.model


import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.home.domain.model.PassengerDetails

data class PassengerDetailsResponse(
    @SerializedName("data")
    val data: PassengerDetailsData? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
){
    fun asDomain():PassengerDetails= PassengerDetails(data?.asDomain(),message, status)
}