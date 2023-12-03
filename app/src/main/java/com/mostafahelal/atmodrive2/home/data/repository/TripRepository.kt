package com.mostafahelal.atmodrive2.home.data.repository

import com.mostafahelal.atmodrive2.home.data.data_source.ITripDataSource
import com.mostafahelal.atmodrive2.home.domain.model.PassengerDetails
import com.mostafahelal.atmodrive2.home.domain.model.TripStatus
import com.mostafahelal.atmodrive2.home.domain.model.UpdateAvailability
import com.mostafahelal.atmodrive2.home.domain.repository.ITripRepository
import com.mostafahelal.atmodrive2.utils.Resource
import javax.inject.Inject

class TripRepository @Inject constructor(private val iTripDataSource: ITripDataSource):ITripRepository {
    override suspend fun acceptTrip(
        trip_id: Int,
        captain_lat: String,
        captain_lng: String,
        captain_location_name: String
    ): Resource<TripStatus> {
        return iTripDataSource.acceptTrip(trip_id, captain_lat, captain_lng, captain_location_name)
    }

    override suspend fun pickupTrip(trip_id: Int): Resource<TripStatus> {
        return  iTripDataSource.pickupTrip(trip_id)
    }

    override suspend fun arrivedTrip(trip_id: Int): Resource<TripStatus> {
        return  iTripDataSource.arrivedTrip(trip_id)
    }

    override suspend fun cancelTrip(trip_id: Int): Resource<TripStatus> {
        return  iTripDataSource.cancelTrip(trip_id)
    }

    override suspend fun startTrip(trip_id: Int): Resource<TripStatus> {
        return  iTripDataSource.startTrip(trip_id)
    }

    override suspend fun endTrip(
        trip_id: Int,
        captain_lat: String,
        captain_lng: String,
        captain_location_name: String,
        distance: Double
    ): Resource<TripStatus> {
        return  iTripDataSource.endTrip(trip_id,captain_lat, captain_lng, captain_location_name, distance)
    }

    override suspend fun getPassengerDetailsForTrip(trip_id: Int): Resource<PassengerDetails> {
        return  iTripDataSource.getPassengerDetailsForTrip(trip_id)
    }

    override suspend fun updateAvailability(
        captain_lat: String,
        captain_lng: String
    ): Resource<UpdateAvailability> {
        return  iTripDataSource.updateAvailability(captain_lat, captain_lng)
    }

    override suspend fun onTrip(): Resource<PassengerDetails> {
        return  iTripDataSource.onTrip()
    }
}