package com.mostafahelal.atmodrive2.home.domain.model

import com.google.gson.annotations.SerializedName

data class Passenger(
    val avatar: String? = null,
    val fullName: String? = null,
    val id: Int? = null,
    val mobile: String? = null,
    val passengerCode: String? = null,
    val rate: Int? = null
)
