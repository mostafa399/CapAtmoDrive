package com.mostafahelal.atmodrive2.home.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TripModel( val captainId:Int,
                      val cost:Double,
                      val distance:Double,
                      val lat:String,
                      val lng:String,
                      val passengerId:Int,
                      val status:String,
                      val tripId:Int,
                      val waitTime:Double
){
    constructor():this(0,0.0,0.0,"","",0,"",0,0.0)
}
