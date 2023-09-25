package com.mostafahelal.atmodrive2.auth.data.data_source.remote
import com.mostafahelal.atmodrive2.auth.data.utils.Resource
import com.mostafahelal.atmodrive2.auth.domain.model.FileUploadResponse
import com.mostafahelal.atmodrive2.auth.domain.model.LoginResponseModel
import com.mostafahelal.atmodrive2.auth.domain.model.RegisterResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface IRemoteAuth {
    suspend fun sendCode(mobile:String): Resource<LoginResponseModel>

    suspend fun checkCode(mobile:String,
                          verification_code:String,
                          device_token:String
    ): Resource<LoginResponseModel>


    suspend fun registerPersonalInfo(mobile:String,
                                     avatar:String,
                                     device_token:String,
                                     device_id:String,
                                     device_type:String,
                                     national_id_front:String,
                                     national_id_back:String,
                                     driving_license_front:String,
                                     driving_license_back:String,
                                     is_dark_mode:Int
    ): Resource<RegisterResponseModel>
    suspend fun registerVehicalInfo(vehicle_front:String,
                                    vehicle_back:String,
                                    vehicle_left:String,
                                    vehicle_right:String,
                                    vehicle_front_seat:String,
                                    vehicle_back_seat:String,
                                    vehicle_license_front:String,
                                    vehicle_license_back:String,
    ): Resource<RegisterResponseModel>

    suspend fun registerBankAccount(bank_name:String,
                                    iban_number:String,
                                    account_name:String,
                                    account_number:String
    ): Resource<RegisterResponseModel>

    suspend fun uploadImage(part: MultipartBody.Part, path: RequestBody): Resource<FileUploadResponse>

}