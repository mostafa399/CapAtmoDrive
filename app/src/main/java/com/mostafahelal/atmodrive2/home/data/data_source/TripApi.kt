package com.mostafahelal.atmodrive2.home.data.data_source

import com.mostafahelal.atmodrive2.home.data.model.PassengerDetailsResponse
import com.mostafahelal.atmodrive2.home.data.model.TripStatusResponse
import com.mostafahelal.atmodrive2.home.data.model.UpdateAvailabilityResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface TripApi {
    @POST("accept-trip")
    @FormUrlEncoded
    suspend fun acceptTrip(
        @Field("trip_id") trip_id: Int,
        @Field("captain_lat") captain_lat: String,
        @Field("captain_lng") captain_lng: String,
        @Field("captain_location_name") captain_location_name: String
    ):TripStatusResponse
    @POST("pickup-trip")
    @FormUrlEncoded
    suspend fun pickupTrip(
        @Field("trip_id") trip_id: Int,
    ):TripStatusResponse
    @POST("arrived-trip")
    @FormUrlEncoded
    suspend fun arrivedTrip(
        @Field("trip_id") trip_id: Int,
    ):TripStatusResponse
    @POST("cancel-trip")
    @FormUrlEncoded
    suspend fun cancelTrip(
        @Field("trip_id") trip_id: Int,
    ):TripStatusResponse
    @POST("start-trip")
    @FormUrlEncoded
    suspend fun startTrip(
        @Field("trip_id") trip_id: Int,
    ):TripStatusResponse
    @POST("end-trip")
    @FormUrlEncoded
    suspend fun endTrip(
        @Field("trip_id") trip_id: Int,
        @Field("captain_lat") captain_lat: String,
        @Field("captain_lng") captain_lng: String,
        @Field("captain_location_name") captain_location_name: String,
        @Field("distance") distance: Double
    ):TripStatusResponse
    @POST("get-passenger-details-for-trip")
    @FormUrlEncoded
    suspend fun getPassengerDetailsForTrip(
        @Field("trip_id") trip_id: Int
    ): PassengerDetailsResponse
    @POST("update-availability")
    @FormUrlEncoded
    suspend fun updateAvailability(
        @Field("captain_lat") captain_lat: String,
        @Field("captain_lng") captain_lng: String
    ): UpdateAvailabilityResponse
    @GET("captain-on-trip")
    suspend fun onTrip():PassengerDetailsResponse

}