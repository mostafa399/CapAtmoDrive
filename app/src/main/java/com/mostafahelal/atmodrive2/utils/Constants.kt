package com.mostafahelal.atmodrive2.utils

import com.google.android.gms.maps.model.LatLng

class Constants {
    companion object{

        const val TRIP_COST = "trip_cost"
        const val BASE_CAPTAIN_URL="https://s1.drive.api.atmosphere.solutions/api/v1/captains/"
        const val BASE_Upload_image_URL="https://s1.drive.dashboard.atmosphere.solutions/"
        const val VEHICLE_IMAGE_PATH="captains"
        const val AVATAR_PREFS = "avatar"
        const val BIRTHDAY_PREFS = "birthday"
        const val CAPTAIN_CODE_PREFS = "captain_code"
        const val EMAIL_PREFS = "email"
        const val FULL_NAME_PREFS = "full_name"
        const val GENDER_PREFS = "gender"
        const val IS_ACTIVE_PREFS = "is_active"
        const val IS_DARK_MODE_PREFS = "is_dark_mode"
        const val LANG_PREFS = "lang"
        const val MOBILE_PREFS = "mobile"
        const val NATIONALITY_PREFS = "nationality"
        const val REGISTER_STEP_PREFS = "register_step"
        const val REMEMBER_TOKEN_PREFS = "remember_token"
        const val STATUS_PREFS = "status"
        const val CAPTAIN_ID = "captainId"
        const val ONLINE_CAPTAINS="OnlineCaptains"
        const val DROPOFFLAT="DROPOFFLAT"
        const val DROPOFFLNG="DROPOFFLNG"
        const val TRIPS="trips"
        var isBottomSheetOn=false
        const val CAPTAIN_STATUS = "captain_status"
        var captainLatLng: LatLng? = null
        var pickUpLatLng: LatLng? = null
        var dropOffLatLng: LatLng? = null



    }
}