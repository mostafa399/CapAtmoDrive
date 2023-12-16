package com.mostafahelal.atmodrive2.home.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.home.domain.use_case.ITripUseCase
import com.mostafahelal.atmodrive2.utils.Constants
import com.mostafahelal.atmodrive2.utils.NetworkState
import com.mostafahelal.atmodrive2.utils.getError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TripViewModel @Inject constructor(private val iTripUseCase: ITripUseCase
,private val preferencesManager: ISharedPreferencesManager)
    :ViewModel(){
    private val _updateAvalibality: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val updateAvalibality: StateFlow<NetworkState?> =_updateAvalibality

    private val _passengerDetails: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val passengerDetails: StateFlow<NetworkState?> =_passengerDetails

    private val _acceptTrip: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val acceptTrip: StateFlow<NetworkState?> =_acceptTrip

    private val _pickUpTrip: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val pickUpTrip: StateFlow<NetworkState?> =_pickUpTrip

    private val _arrivedTrip: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val arrivedTrip: StateFlow<NetworkState?> =_arrivedTrip

    private val _startTrip: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val startTrip: StateFlow<NetworkState?> =_startTrip

    private val _cancelTrip: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val cancelTrip: StateFlow<NetworkState?> =_cancelTrip

    private val _endTrip: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val endTrip: StateFlow<NetworkState?> =_endTrip

    private val _onTrip: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val onTrip: StateFlow<NetworkState?> =_onTrip

    private val _confirmCash: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val confirmCash: StateFlow<NetworkState?> =_confirmCash

    fun updateAvailability(captainLat: String,captainLng:String) {
        _updateAvalibality.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iTripUseCase.updateAvailability(captainLat, captainLng)
                if (result.data?.status!!){
                    _updateAvalibality.value = NetworkState.getLoaded(result)
                }else{
                    _updateAvalibality.value = NetworkState.getErrorMessage(result.message.toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _updateAvalibality.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

    fun getPassengerDetails(tripId:Int) {
        _passengerDetails.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iTripUseCase.getPassengerDetailsForTrip(tripId)
                if (result.isSuccessful()){
                    _passengerDetails.value = NetworkState.getLoaded(result)
                    preferencesManager.saveString(Constants.DROPOFFLAT,result.data?.data?.dropoffLat)
                    preferencesManager.saveString(Constants.DROPOFFLNG,result.data?.data?.dropoffLng)
                }else{
                    _passengerDetails.value = NetworkState.getErrorMessage(result.message.toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _passengerDetails.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

    fun acceptTrip(tripId: Int,captainLat: String,captainLng: String,captainLocName: String) {
        _acceptTrip.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iTripUseCase.acceptTrip(tripId,captainLat, captainLng, captainLocName)
                if (result.isSuccessful()){
                    _acceptTrip.value = NetworkState.getLoaded(result)
                }else{
                    _acceptTrip.value = NetworkState.getErrorMessage(result.message.toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _acceptTrip.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

    fun pickUpTrip(tripId: Int) {
        _pickUpTrip.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iTripUseCase.pickupTrip(tripId)
                if (result.isSuccessful()){
                    _pickUpTrip.value = NetworkState.getLoaded(result)
                }else{
                    _pickUpTrip.value = NetworkState.getErrorMessage(result.message.toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _pickUpTrip.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

    fun arrivedTrip(tripId: Int) {
        _arrivedTrip.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iTripUseCase.arrivedTrip(tripId)
                if (result.isSuccessful()){
                    _arrivedTrip.value = NetworkState.getLoaded(result)
                }else{
                    _arrivedTrip.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _arrivedTrip.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

    fun startTrip(tripId: Int) {
        _startTrip.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iTripUseCase.startTrip(tripId)
                if (result.isSuccessful()){
                    _startTrip.value = NetworkState.getLoaded(result)
                }else{
                    _startTrip.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _startTrip.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

    fun cancelTrip(tripId: Int) {
        _cancelTrip.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iTripUseCase.cancelTrip(tripId)
                if (result.isSuccessful()){
                    _cancelTrip.value = NetworkState.getLoaded(result)
                }else{
                    _cancelTrip.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _cancelTrip.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

    fun endTrip(
        tripId: Int,
        dropOffLat: Double,
        dropOffLng: Double,
        dropOffLocName: String,
        distance: Double
    ) {
        _endTrip.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iTripUseCase.endTrip(tripId, dropOffLat, dropOffLng, dropOffLocName, distance)
                if (result.isSuccessful()){
                    _endTrip.value = NetworkState.getLoaded(result)
                }else{
                    _endTrip.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _endTrip.value = NetworkState.getErrorMessage(ex)
            }
        }
    }
    fun onTrip() {
        _onTrip.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iTripUseCase.onTrip()
                if (result.isSuccessful()){
                    _onTrip.value = NetworkState.getLoaded(result)
                }else{
                    _onTrip.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _onTrip.value = NetworkState.getErrorMessage(ex)
            }
        }
    }
    fun confirmCash(
        tripId: Int,
        amount: Double    ) {
        _confirmCash.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iTripUseCase.confirmCash(tripId,amount)
                if (result.isSuccessful()){
                    _confirmCash.value = NetworkState.getLoaded(result)
                }else{
                    _confirmCash.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _confirmCash.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

}
