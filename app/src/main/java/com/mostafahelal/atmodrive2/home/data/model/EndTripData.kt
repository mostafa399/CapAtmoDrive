package com.mostafahelal.atmodrive2.home.data.model


import com.google.gson.annotations.SerializedName
import com.mostafahelal.atmodrive2.home.domain.model.CostData

data class EndTripData(
    @SerializedName("cost")
    val cost: Double? = null,
    @SerializedName("id")
    val id: Int? = null
){
    fun asDomain():CostData= CostData(cost, id)
}