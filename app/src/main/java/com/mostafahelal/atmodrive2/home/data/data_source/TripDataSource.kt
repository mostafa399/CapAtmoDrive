package com.mostafahelal.atmodrive2.home.data.data_source

import com.mostafahelal.atmodrive2.home.domain.model.PassengerDetails
import com.mostafahelal.atmodrive2.home.domain.model.TripStatus
import com.mostafahelal.atmodrive2.home.domain.model.UpdateAvailability
import com.mostafahelal.atmodrive2.utils.NetworkState
import com.mostafahelal.atmodrive2.utils.Resource
import javax.inject.Inject

class TripDataSource @Inject constructor(val tripApi: TripApi):ITripDataSource {
    override suspend fun acceptTrip(
        trip_id: Int,
        captain_lat: String,
        captain_lng: String,
        captain_location_name: String
    ): Resource<TripStatus> {
        return try {
            val acceptTrip = tripApi.acceptTrip(trip_id, captain_lat, captain_lng, captain_location_name)
            if (acceptTrip.status){
                return Resource.Success(acceptTrip.asDomain())
            }else{
                return Resource.Error(acceptTrip.message)
            }
        }catch (e: Exception){
            Resource.Error(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun pickupTrip(trip_id: Int): Resource<TripStatus> {
        return try {
            val pickupTrip = tripApi.pickupTrip(trip_id)
            if (pickupTrip.status){
                return Resource.Success(pickupTrip.asDomain())
            }else{
                return Resource.Error(pickupTrip.message)
            }
        }catch (e: Exception){
            Resource.Error(NetworkState.getErrorMessage(e).msg.toString())
        }    }

    override suspend fun arrivedTrip(trip_id: Int): Resource<TripStatus> {
        return try {
            val arrivedTrip = tripApi.arrivedTrip(trip_id)
            if (arrivedTrip.status){
                return Resource.Success(arrivedTrip.asDomain())
            }else{
                return Resource.Error(arrivedTrip.message)
            }
        }catch (e: Exception){
            Resource.Error(NetworkState.getErrorMessage(e).msg.toString())
        }      }

    override suspend fun cancelTrip(trip_id: Int): Resource<TripStatus> {
        return try {
            val cancelTrip = tripApi.cancelTrip(trip_id)
            if (cancelTrip.status){
                return Resource.Success(cancelTrip.asDomain())
            }else{
                return Resource.Error(cancelTrip.message)
            }
        }catch (e: Exception){
            Resource.Error(NetworkState.getErrorMessage(e).msg.toString())
        }      }

    override suspend fun startTrip(trip_id: Int): Resource<TripStatus> {
        return try {
            val startTrip = tripApi.startTrip(trip_id)
            if (startTrip.status){
                return Resource.Success(startTrip.asDomain())
            }else{
                return Resource.Error(startTrip.message)
            }
        }catch (e: Exception){
            Resource.Error(NetworkState.getErrorMessage(e).msg.toString())
        }      }

    override suspend fun endTrip(
        trip_id: Int,
        captain_lat: String,
        captain_lng: String,
        captain_location_name: String,
        distance: Double
    ): Resource<TripStatus> {
        return try {
            val endTrip = tripApi.endTrip(trip_id,captain_lat, captain_lng, captain_location_name, distance)
            if (endTrip.status){
                return Resource.Success(endTrip.asDomain())
            }else{
                return Resource.Error(endTrip.message)
            }
        }catch (e: Exception){
            Resource.Error(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun getPassengerDetailsForTrip(trip_id: Int): Resource<PassengerDetails> {
        return try {
            val getPassengerDetailsForTrip = tripApi.getPassengerDetailsForTrip(trip_id)
            if (getPassengerDetailsForTrip.status){
                return Resource.Success(getPassengerDetailsForTrip.asDomain())
            }else{
                return Resource.Error(getPassengerDetailsForTrip.message)
            }
        }catch (e: Exception){
            Resource.Error(NetworkState.getErrorMessage(e).msg.toString())
        }      }

    override suspend fun updateAvailability(
        captain_lat: String,
        captain_lng: String
    ): Resource<UpdateAvailability> {
        return try {
            val updateAvailability = tripApi.updateAvailability(captain_lat, captain_lng)
            if (updateAvailability.status){
                return Resource.Success(updateAvailability.asDomain())
            }else{
                return Resource.Error(updateAvailability.message)
            }
        }catch (e: Exception){
            Resource.Error(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun onTrip(): Resource<PassengerDetails> {
        return try {
            val onTrip = tripApi.onTrip()
            if (onTrip.status){
                return Resource.Success(onTrip.asDomain())
            }else{
                return Resource.Error(onTrip.message)
            }
        }catch (e: Exception){
            Resource.Error(NetworkState.getErrorMessage(e).msg.toString())
        }    }
}