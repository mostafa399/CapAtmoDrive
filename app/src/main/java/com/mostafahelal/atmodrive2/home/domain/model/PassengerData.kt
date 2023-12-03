package com.mostafahelal.atmodrive2.home.domain.model

import com.mostafahelal.atmodrive2.home.data.model.DataPassenger

data class PassengerData( val cost: String? = null,
                          val createdAt: String? = null,
                          val dropoffLat: String? = null,
                          val dropoffLng: String? = null,
                          val dropoffLocationName: String? = null,
                          val pickup_lat: String? = null,
                          val pickup_lng: String? = null,
                          val pickup_location_name: String? = null,
                          val estimateTime: String? = null,
                          val id: Int? = null,
                          val tripCode: String? = null,
                          val tripColor: String? = null,
                          val tripStatus: String? = null,
                          val passenger: Passenger? = null
)
