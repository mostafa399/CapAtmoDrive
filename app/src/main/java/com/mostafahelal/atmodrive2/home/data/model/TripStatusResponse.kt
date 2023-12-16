package com.mostafahelal.atmodrive2.home.data.model


import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.home.domain.model.TripStatus

data class TripStatusResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    val data:EndTripData?
){
    fun asDomain():TripStatus= TripStatus(message, status,data?.asDomain())
}