package com.mostafahelal.atmodrive2.home.data

import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties
data class CaptainModel(val id: String, val tripId: Int
                     , val lat: String, val lng: String)
{
    constructor():this("1",0,"30.369852","30147852")
}
