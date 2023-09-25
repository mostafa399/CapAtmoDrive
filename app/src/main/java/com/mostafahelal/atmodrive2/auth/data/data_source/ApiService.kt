package com.mostafahelal.atmodrive2.auth.data.data_source

import com.mostafahelal.atmodrive2.auth.data.models.LoginResponse
import com.mostafahelal.atmodrive2.auth.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("send-code")
    suspend fun sendCode(@Field("mobile")mobile:String):Response<LoginResponse>


    @FormUrlEncoded
    @POST("check-code")
    suspend fun checkCode(@Field("mobile")mobile:String,
                          @Field("verification_code")verification_code:String,
                          @Field("device_token")device_token:String):Response<LoginResponse>

    @FormUrlEncoded
    @POST("register-captain")
    suspend fun registerPersonalInfo(@Field("mobile")mobile:String,
                                     @Field("avatar")avatar:String,
                                     @Field("device_token")device_token:String,
                                     @Field("device_id")device_id:String,
                                     @Field("device_type")device_type:String,
                                     @Field("national_id_front")national_id_front:String,
                                     @Field("national_id_back")national_id_back:String,
                                     @Field("driving_license_front")driving_license_front:String,
                                     @Field("driving_license_back")driving_license_back:String,
                                     @Field("is_dark_mode")is_dark_mode:Int
                                ):Response<RegisterResponse>
    @FormUrlEncoded
    @POST("register-vehicle")
    suspend fun registerVehicalInfo(@Field("vehicle_front")vehicle_front:String,
                                     @Field("vehicle_back")vehicle_back:String,
                                     @Field("vehicle_left")vehicle_left:String,
                                     @Field("vehicle_right")vehicle_right:String,
                                     @Field("vehicle_front_seat")vehicle_front_seat:String,
                                     @Field("vehicle_back_seat")vehicle_back_seat:String,
                                     @Field("vehicle_license_front")vehicle_license_front:String,
                                     @Field("vehicle_license_back")vehicle_license_back:String,
                                ):Response<RegisterResponse>
    @FormUrlEncoded
    @POST("register-bank-account")
    suspend fun registerBankAccount(@Field("bank_name")bank_name:String?,
                                     @Field("iban_number")iban_number:String?,
                                     @Field("account_name")account_name:String?,
                                     @Field("account_number")account_number:String?
                                ):Response<RegisterResponse>


}