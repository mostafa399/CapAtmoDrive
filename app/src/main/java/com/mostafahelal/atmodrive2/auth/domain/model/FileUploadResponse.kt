package com.mostafahelal.atmodrive2.auth.domain.model


import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("data")
    val data: String?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)