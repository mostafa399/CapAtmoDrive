package com.mostafahelal.atmodrive2.auth.presentation.view_model
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.auth.data.utils.Constants
import com.mostafahelal.atmodrive2.auth.data.utils.NetworkState
import com.mostafahelal.atmodrive2.auth.data.utils.Resource
import com.mostafahelal.atmodrive2.auth.data.utils.getData
import com.mostafahelal.atmodrive2.auth.domain.model.LoginResponseModel
import com.mostafahelal.atmodrive2.auth.domain.model.RegisterResponseModel
import com.mostafahelal.atmodrive2.auth.domain.use_case.IAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val iAuthUseCase: IAuthUseCase,
    private val iSharedPreferencesManager: ISharedPreferencesManager
):ViewModel() {
    private val _sendCodeResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val sendCodeResult: StateFlow<NetworkState?> = _sendCodeResult
    private val _navigateToRegister: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val navigateToRegister: StateFlow<NetworkState?> = _navigateToRegister
    private val _navigateToMain: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val navigateToMain: StateFlow<NetworkState?> = _navigateToMain
    private val _registerState: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val registerState: StateFlow<NetworkState?> = _registerState
    private val _registerVehicalState: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val registerVehicalState: StateFlow<NetworkState?> = _registerVehicalState
    private val _mainEvent: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val mainEvent: StateFlow<NetworkState?> = _mainEvent
    private val _bankAccount: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val bankAccount: StateFlow<NetworkState?> = _bankAccount


   fun uploadImage(part:MultipartBody.Part,path:RequestBody){
       _mainEvent.value=NetworkState.LOADING
       viewModelScope.launch {
           try {
               val response =iAuthUseCase.uploadImage(part,path)
               if (response.isSuccessful()) {
                   _mainEvent.value = NetworkState.getLoaded(response)
               } else {
                   _mainEvent.value = NetworkState.getErrorMessage( "Failed to upload image.")
               }
           } catch (e: Exception) {
               e.printStackTrace()
               _sendCodeResult.value = NetworkState.getErrorMessage(e)
           }
       }
   }

    fun sendMobilePhone(mobile: String) {
        viewModelScope.launch {
            _sendCodeResult.value = NetworkState.LOADING
            try {
                val response =iAuthUseCase.sendCode(mobile)
                if (response.data?.status!!) {
                    _sendCodeResult.value = NetworkState.getLoaded(response.data)
                } else if (response.isFailed()){
                    _sendCodeResult.value = NetworkState.getErrorMessage("API request failed Send code request failed")
                }
            } catch (e: Exception) {
                _sendCodeResult.value = NetworkState.getErrorMessage(e)
            }
        }
    }

    fun checkCode(  mobile: String, verification_code: String, device_token: String){
        viewModelScope.launch {
            try {
                val response=iAuthUseCase.checkCode(mobile,verification_code,device_token)
                if (response.data?.status!!){
                    _navigateToRegister.value = NetworkState.getLoaded(response)
                    saveLoginData(response)
                    iSharedPreferencesManager.saveUserAccessToken("${response.getData()?.data?.user?.remember_token}")
                }
                else {
                    _navigateToRegister.value=NetworkState.getErrorMessage("Error Code !!")
                }

            }catch (e:Exception){
                _navigateToRegister.value = NetworkState.getErrorMessage(e)

            }
        }


}


    fun registerCaptain( mobile: String,
                         avatar: String,
                         device_token: String,
                         device_id: String,
                         device_type: String,
                         national_id_front: String,
                         national_id_back: String,
                         driving_license_front: String,
                         driving_license_back: String,
                         is_dark_mode: Int)
    {


            viewModelScope.launch {
                _registerState.value = NetworkState.LOADING
                try {

                    val response=iAuthUseCase.registerPersonalInfo(mobile,avatar,device_token,device_id,device_type, national_id_front,national_id_back,driving_license_front,driving_license_back,is_dark_mode)

                    if (response.isSuccessful()){
                        _registerState.value = NetworkState.getLoaded(response)
                        saveData(response)
                    }
                    else if (response.isFailed()){
                       _registerState.value = NetworkState.getErrorMessage(response.message.toString())

            }

                }catch (e:Exception){

                    _registerState.value = NetworkState.getErrorMessage(e)

                }

                }

    }
     fun registerVehical(vehicle_front:String,
                                vehicle_back:String,
                                vehicle_left:String,
                                vehicle_right:String,
                                vehicle_front_seat:String,
                                vehicle_back_seat:String,
                                vehicle_license_front:String,
                                vehicle_license_back:String,){
        _registerVehicalState.value=NetworkState.LOADING
        try {
            viewModelScope.launch {
                val response=iAuthUseCase.registerVehicalInfo(vehicle_front,vehicle_back, vehicle_left, vehicle_right, vehicle_front_seat, vehicle_back_seat, vehicle_license_front, vehicle_license_back)
               if (response.isSuccessful()){
                   _registerVehicalState.value=NetworkState.getLoaded(response)
                   saveData(response)

               }else{
                   _registerVehicalState.value=NetworkState.getErrorMessage(response.message.toString())

               }

            }

        }catch (e:Exception){
            _registerVehicalState.value=NetworkState.getErrorMessage(e)
        }


    }


    fun registerBankAccount(bank_name:String,
                            iban_number:String,
                            account_name:String,
                            account_number:String){
        _bankAccount.value=NetworkState.LOADING
        try {
            viewModelScope.launch {
                val response=iAuthUseCase.registerBankAccount(bank_name,iban_number, account_name, account_number)
                if (response.isSuccessful()){
                    _bankAccount.value=NetworkState.getLoaded(response)
                    saveData(response)
                }else{
                    _bankAccount.value=NetworkState.getErrorMessage(response.message.toString())

                }

            }

        }catch (e:Exception){
            _bankAccount.value=NetworkState.getErrorMessage(e)
        }

    }


    fun saveLoginData(userData : Resource<LoginResponseModel>){
        with(iSharedPreferencesManager) {
            saveString(Constants.AVATAR_PREFS, userData.getData()?.data?.user?.avatar)
            saveString(Constants.EMAIL_PREFS, userData.getData()?.data?.user?.email)
            saveString(Constants.FULL_NAME_PREFS,userData.getData()?.data?.user?.full_name)
            saveString(Constants.IS_DARK_MODE_PREFS,userData.getData()?.data?.user?.is_dark_mode.toString())
            saveString(Constants.LANG_PREFS, userData.getData()?.data?.user?.lang)
            saveString(Constants.MOBILE_PREFS, userData.getData()?.data?.user?.mobile)
            saveString(Constants.CAPTAIN_CODE_PREFS, userData.getData()?.data?.user?.captain_code)
            saveString(Constants.BIRTHDAY_PREFS,userData.getData()?.data?.user?.birthday)
            saveString(Constants.REMEMBER_TOKEN_PREFS, userData.getData()?.data?.user?.remember_token)
            saveString(Constants.GENDER_PREFS,userData.getData()?.data?.user?.gender)
            saveString(Constants.STATUS_PREFS,userData.getData()?.data?.user?.status.toString())
            saveString(Constants.IS_ACTIVE_PREFS,userData.getData()?.data?.user?.is_active.toString())
            saveString(Constants.NATIONALITY_PREFS,userData.getData()?.data?.user?.nationality)
            saveString(Constants.REGISTER_STEP_PREFS,userData.getData()?.data?.user?.register_step.toString())
        }
    }
    fun saveData(response:Resource<RegisterResponseModel>){
        iSharedPreferencesManager.saveString(Constants.AVATAR_PREFS,response.data?.data?.avatar)
        iSharedPreferencesManager.saveString(Constants.EMAIL_PREFS, response.getData()?.data?.email)
        iSharedPreferencesManager.saveString(Constants.FULL_NAME_PREFS,response.getData()?.data?.full_name)
        iSharedPreferencesManager.saveString(Constants.IS_DARK_MODE_PREFS,response.getData()?.data?.is_dark_mode.toString())
        iSharedPreferencesManager. saveString(Constants.LANG_PREFS, response.getData()?.data?.lang)
        iSharedPreferencesManager.saveString(Constants.MOBILE_PREFS, response.getData()?.data?.mobile)
        iSharedPreferencesManager.saveString(Constants.CAPTAIN_CODE_PREFS, response.getData()?.data?.captain_code)
        iSharedPreferencesManager.saveString(Constants.BIRTHDAY_PREFS,response.getData()?.data?.birthday)
        iSharedPreferencesManager.saveString(Constants.REMEMBER_TOKEN_PREFS, response.getData()?.data?.remember_token)
        iSharedPreferencesManager.saveString(Constants.GENDER_PREFS,response.getData()?.data?.gender)
        iSharedPreferencesManager.saveString(Constants.STATUS_PREFS,response.getData()?.data?.status.toString())
        iSharedPreferencesManager.saveString(Constants.IS_ACTIVE_PREFS,response.getData()?.data?.is_active.toString())
        iSharedPreferencesManager.saveString(Constants.NATIONALITY_PREFS,response.getData()?.data?.nationality)
        iSharedPreferencesManager.saveString(Constants.REGISTER_STEP_PREFS,response.getData()?.data?.register_step.toString())

    }

}