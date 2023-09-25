package com.mostafahelal.atmodrive2.auth.data.models


import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.auth.domain.model.OptionsLogin

data class Options(
    @SerializedName("brands")
    val brands: List<String>,
    @SerializedName("colors")
    val colors: List<String> ,
    @SerializedName("device_types")
    val deviceTypes: List<String> ,
    @SerializedName("gender")
    val gender: List<String>,
    @SerializedName("years")
    val years: List<String>
){
    fun asDomain():OptionsLogin = OptionsLogin(
        brands,
        colors,
        deviceTypes,
        gender,
        years
    )
}