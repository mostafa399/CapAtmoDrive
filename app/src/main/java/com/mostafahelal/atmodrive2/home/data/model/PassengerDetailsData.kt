package com.mostafahelal.atmodrive2.home.data.model


import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.home.domain.model.PassengerData

data class PassengerDetailsData(
    @SerializedName("cost")
    val cost: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("dropoff_lat")
    val dropoffLat: String? = null,
    @SerializedName("dropoff_lng")
    val dropoffLng: String? = null,
    @SerializedName("dropoff_location_name")
    val dropoffLocationName: String? = null,
    @SerializedName("pickup_lat")
    val pickup_lat: String? = null,
    @SerializedName("pickup_lng")
    val pickup_lng: String? = null,
    @SerializedName("pickup_location_name")
    val pickup_location_name: String? = null,
    @SerializedName("estimate_time")
    val estimateTime: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("trip_code")
    val tripCode: String? = null,
    @SerializedName("trip_color")
    val tripColor: String? = null,
    @SerializedName("trip_status")
    val tripStatus: String? = null,
    @SerializedName("passenger")
    val passenger: DataPassenger? = null
){
    fun asDomain():PassengerData= PassengerData(cost, createdAt, dropoffLat, dropoffLng, dropoffLocationName,pickup_lat,pickup_lng,pickup_location_name, estimateTime, id, tripCode, tripColor, tripStatus,
        passenger!!.asDomain())
}