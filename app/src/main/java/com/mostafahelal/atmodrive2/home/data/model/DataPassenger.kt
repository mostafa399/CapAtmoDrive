package com.mostafahelal.atmodrive2.home.data.model


import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.home.domain.model.Passenger

data class DataPassenger(
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("mobile")
    val mobile: String? = null,
    @SerializedName("passenger_code")
    val passengerCode: String? = null,
    @SerializedName("rate")
    val rate: Int? = null
){
    fun asDomain():Passenger=Passenger(avatar, fullName, id, mobile, passengerCode, rate)
}