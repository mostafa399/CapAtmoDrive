package com.mostafahelal.atmodrive2.home.model

import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties
data class CaptainModel(val id: String?= null, val tripId: String?= null
                     , val lat: String?= null, val lng: String?= null)
