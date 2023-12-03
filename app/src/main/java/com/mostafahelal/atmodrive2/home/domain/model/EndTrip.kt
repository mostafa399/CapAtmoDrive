package com.mostafahelal.atmodrive2.home.domain.model

data class EndTrip(
    val data: CostData = CostData(),
    val message: String? = "",
    val status: Boolean? = false)
