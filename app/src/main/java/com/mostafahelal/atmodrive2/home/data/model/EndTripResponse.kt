package com.mostafahelal.atmodrive2.home.data.model


import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.home.domain.model.EndTrip

data class EndTripResponse(
    @SerializedName("data")
    val data: EndTripData? = EndTripData(),
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("status")
    val status: Boolean? = false
){
    fun asDomain():EndTrip= EndTrip(data!!.asDomain(),message, status)
}