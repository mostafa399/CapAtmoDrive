package com.mostafahelal.atmodrive2.home.presenter.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.Locale

class SharedViewModel:ViewModel() {
    val requestStatus = MutableLiveData<Boolean>()
    val tripId = MutableLiveData<Int>()
    val currentLocation = MutableLiveData<HashMap<String,Any>>()
    val tripStatus = MutableLiveData<Boolean>()
     fun setRequestStatus(status: Boolean) {
        requestStatus.value = status
    }
    fun setTripId(id: Int) {
        tripId.value = id
    }
    fun setCurrentLocation(loc:HashMap<String,Any>){
        currentLocation.value = loc
    }
    fun setTripStatus(Status:Boolean){
        tripStatus.value = Status
    }
    fun getAddressFromLatLng(context: Context,latLng: LatLng): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var address = ""
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val firstAddress: Address = addresses[0]
                val stringBuilder = StringBuilder()

                for (i in 0..firstAddress.maxAddressLineIndex) {
                    stringBuilder.append(firstAddress.getAddressLine(i))
                    if (i < firstAddress.maxAddressLineIndex) {
                        stringBuilder.append(", ")
                    }
                }

                address = stringBuilder.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return address
    }
}