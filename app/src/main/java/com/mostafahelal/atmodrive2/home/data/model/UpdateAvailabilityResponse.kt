package com.mostafahelal.atmodrive2.home.data.model


import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.home.domain.model.UpdateAvailability

data class UpdateAvailabilityResponse(
    @SerializedName("available")
    val available: Boolean? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
){fun asDomain():UpdateAvailability=
    UpdateAvailability(available, message, status)
}