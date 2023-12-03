package com.mostafahelal.atmodrive2.home.domain.model

import com.google.gson.annotations.SerializedName

data class UpdateAvailability(

    val available: Boolean? = null,
    val message: String,
    val status: Boolean
)
