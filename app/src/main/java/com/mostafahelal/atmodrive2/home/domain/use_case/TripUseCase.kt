package com.mostafahelal.atmodrive2.home.domain.use_case

import com.mostafahelal.atmodrive2.home.domain.model.PassengerDetails
import com.mostafahelal.atmodrive2.home.domain.model.TripStatus
import com.mostafahelal.atmodrive2.home.domain.model.UpdateAvailability
import com.mostafahelal.atmodrive2.home.domain.repository.ITripRepository
import com.mostafahelal.atmodrive2.utils.Resource
import javax.inject.Inject

class TripUseCase @Inject constructor(private val iTripRepository: ITripRepository):ITripUseCase {
    override suspend fun acceptTrip(
        trip_id: Int,
        captain_lat: String,
        captain_lng: String,
        captain_location_name: String
    ): Resource<TripStatus> {
        return iTripRepository.acceptTrip(trip_id, captain_lat, captain_lng, captain_location_name)
    }

    override suspend fun pickupTrip(trip_id: Int): Resource<TripStatus> {
        return iTripRepository.pickupTrip(trip_id)
    }

    override suspend fun arrivedTrip(trip_id: Int): Resource<TripStatus> {
        return iTripRepository.arrivedTrip(trip_id)
    }

    override suspend fun cancelTrip(trip_id: Int): Resource<TripStatus> {
        return iTripRepository.cancelTrip(trip_id)
    }

    override suspend fun startTrip(trip_id: Int): Resource<TripStatus> {
        return iTripRepository.startTrip(trip_id)
    }

    override suspend fun endTrip(
        trip_id: Int,
        captain_lat: Double,
        captain_lng: Double,
        captain_location_name: String,
        distance: Double
    ): Resource<TripStatus> {
        return iTripRepository.endTrip(trip_id, captain_lat, captain_lng, captain_location_name, distance)
    }

    override suspend fun getPassengerDetailsForTrip(trip_id: Int): Resource<PassengerDetails> {
        return iTripRepository.getPassengerDetailsForTrip(trip_id)
            }

    override suspend fun updateAvailability(
        captain_lat: String,
        captain_lng: String
    ): Resource<UpdateAvailability> {

        return iTripRepository.updateAvailability(captain_lat, captain_lng)
    }

    override suspend fun onTrip(): Resource<PassengerDetails> {
        return iTripRepository.onTrip()
    }

    override suspend fun confirmCash(tripId: Int, amount: Double): Resource<TripStatus> {
        return iTripRepository.confirmCash(tripId, amount)
    }
}