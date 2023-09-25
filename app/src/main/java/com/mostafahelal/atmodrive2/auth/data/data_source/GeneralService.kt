package com.mostafahelal.atmodrive2.auth.data.data_source
import com.mostafahelal.atmodrive2.auth.domain.model.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface GeneralService {
    @Multipart
    @POST("api/upload-files")
    suspend fun uploadImage(
        @Part part: MultipartBody.Part,
        @Part ("path")path:RequestBody
    ):FileUploadResponse
}