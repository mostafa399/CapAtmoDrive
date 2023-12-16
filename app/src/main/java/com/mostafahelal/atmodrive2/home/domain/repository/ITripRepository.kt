package com.mostafahelal.atmodrive2.home.domain.repository

import com.mostafahelal.atmodrive2.home.domain.model.PassengerDetails
import com.mostafahelal.atmodrive2.home.domain.model.TripStatus
import com.mostafahelal.atmodrive2.home.domain.model.UpdateAvailability
import com.mostafahelal.atmodrive2.utils.Resource

interface ITripRepository {
    suspend fun acceptTrip(
        trip_id: Int,
        captain_lat: String,
        captain_lng: String,
        captain_location_name: String
    ): Resource<TripStatus>
    suspend fun pickupTrip(
        trip_id: Int,
    ): Resource<TripStatus>
    suspend fun arrivedTrip(
        trip_id: Int,
    ): Resource<TripStatus>

    suspend fun cancelTrip(
        trip_id: Int,
    ): Resource<TripStatus>

    suspend fun startTrip(
        trip_id: Int,
    ): Resource<TripStatus>
    suspend fun endTrip(
        trip_id: Int,
        captain_lat: Double,
        captain_lng: Double,
        captain_location_name: String,
        distance: Double
    ): Resource<TripStatus>
    suspend fun getPassengerDetailsForTrip(
        trip_id: Int
    ): Resource<PassengerDetails>
    suspend fun updateAvailability(
        captain_lat: String,
        captain_lng: String
    ): Resource<UpdateAvailability>
    suspend fun onTrip():Resource<PassengerDetails>
    suspend fun confirmCash(
        tripId: Int,
        amount: Double
    ): Resource<TripStatus>

}