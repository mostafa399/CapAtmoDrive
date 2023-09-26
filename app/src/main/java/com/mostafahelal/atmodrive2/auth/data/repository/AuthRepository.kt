package com.mostafahelal.atmodrive2.auth.data.repository

import com.mostafahelal.atmodrive2.auth.data.data_source.remote.IRemoteAuth
import com.mostafahelal.atmodrive2.auth.data.utils.Resource
import com.mostafahelal.atmodrive2.auth.domain.model.FileUploadResponse
import com.mostafahelal.atmodrive2.auth.domain.model.LoginResponseModel
import com.mostafahelal.atmodrive2.auth.domain.model.RegisterResponseModel
import com.mostafahelal.atmodrive2.auth.domain.repository.IAuthRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepository @Inject constructor(private val iRemoteAuth: IRemoteAuth):IAuthRepository {
    override suspend fun sendCode(mobile: String): Resource<LoginResponseModel> {
       return  iRemoteAuth.sendCode(mobile)
    }

    override suspend fun checkCode(
        mobile: String,
        verification_code: String,
        device_token: String
    ): Resource<LoginResponseModel> {
       return iRemoteAuth.checkCode(mobile,verification_code,device_token)

    }

    override suspend fun registerPersonalInfo(
        mobile: String,
        avatar: String,
        device_token: String,
        device_id: String,
        device_type: String,
        national_id_front: String,
        national_id_back: String,
        driving_license_front: String,
        driving_license_back: String,
        is_dark_mode: Int
    ): Resource<RegisterResponseModel> {
        return try {
            val response=iRemoteAuth.registerPersonalInfo(mobile,avatar,device_token,device_id,
                device_type,national_id_front,national_id_back,driving_license_front,driving_license_back,is_dark_mode)
            if (response.isSuccessful() && response.data?.status == true) {
                 Resource.Success(response.data)
            } else {
                 Resource.Error("Send code request failed")
            }
        } catch (e: Exception) {
             Resource.Error(e.localizedMessage)
        }
    }

    override suspend fun registerVehicalInfo(
        vehicle_front: String,
        vehicle_back: String,
        vehicle_left: String,
        vehicle_right: String,
        vehicle_front_seat: String,
        vehicle_back_seat: String,
        vehicle_license_front: String,
        vehicle_license_back: String
    ): Resource<RegisterResponseModel> {
        return iRemoteAuth.registerVehicalInfo(vehicle_front,vehicle_back,vehicle_left,vehicle_right,
                vehicle_front_seat,vehicle_back_seat,vehicle_license_front,vehicle_license_back)
    }

    override suspend fun registerBankAccount(
        bank_name: String,
        iban_number: String,
        account_name: String,
        account_number: String
    ): Resource<RegisterResponseModel> {
       return iRemoteAuth.registerBankAccount(bank_name,iban_number,account_name,account_number)
    }

    override suspend fun uploadImage(
        part: MultipartBody.Part,
        path: RequestBody
    ): Resource<FileUploadResponse> {
        return  iRemoteAuth.uploadImage(part,path)
    }
}