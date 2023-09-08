package com.mostafahelal.atmodrive2.auth.presentation.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeFile
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mostafahelal.atmodrive2.auth.data.models.UploadImageResponse
import com.mostafahelal.atmodrive2.auth.data.utils.Constants
import com.mostafahelal.atmodrive2.auth.data.utils.NetworkState
import com.mostafahelal.atmodrive2.auth.data.utils.Resource
import com.mostafahelal.atmodrive2.auth.data.utils.disable
import com.mostafahelal.atmodrive2.auth.data.utils.enabled
import com.mostafahelal.atmodrive2.auth.data.utils.getData
import com.mostafahelal.atmodrive2.auth.data.utils.showToast
import com.mostafahelal.atmodrive2.auth.data.utils.visibilityGone
import com.mostafahelal.atmodrive2.auth.data.utils.visibilityVisible
import com.mostafahelal.atmodrive2.auth.presentation.view_model.AuthViewModel
import com.mostafahelal.atmodrive2.databinding.FragmentCreateAccountPersonalInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CreateAccountPersonalInfoFragment : Fragment() {
    private var imageUploading = ""
    private var avatar = ""
    private var idFront = ""
    private var idBack = ""
    private var personalPhoto:Uri? = null
    private var idFrontPhoto:Uri? = null
    private var idBackPhoto:Uri? = null
    private var imageType = ""
    private val viewModel :AuthViewModel by viewModels()
    private lateinit var binding: FragmentCreateAccountPersonalInfoBinding
    private val args by navArgs<CreateAccountPersonalInfoFragmentArgs>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCreateAccountPersonalInfoBinding.inflate(layoutInflater)
        return binding.root
       }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentCreateAccountPersonalInfoBinding.bind(view)

        binding.personalCaptainImage.setOnClickListener {
            imageType  = "PersonalPhoto"
            setUpImagePicker()
        }
        binding.deletePersonalPhoto.setOnClickListener {
            binding.apply {
                imgPersonalPhoto.setImageURI(null)
                deletePersonalPhoto.visibilityGone()
                uploadPersonalPhoto.visibilityGone()
                personalCaptainImage.enabled()
            }
            avatar = ""
        }
        binding.uploadPersonalPhoto.setOnClickListener {
            if (personalPhoto != null){
                uploadImage(personalPhoto!!)
                imageUploading = "PersonalPhoto"
            }
        }


        /////////////////////
        binding.nationalIdFrontImage.setOnClickListener {
            imageType  = "nationalIdFrontImage"
            setUpImagePicker()
        }
        binding.deleteIdFront.setOnClickListener {
            binding.apply {
                imgIdFront.setImageURI(null)
                deleteIdFront.visibilityGone()
                uploadIdFront.visibilityGone()
                nationalIdFrontImage.enabled()
            }
            idFront = ""
        }
        binding.uploadIdFront.setOnClickListener {
            if (idFrontPhoto != null){
                uploadImage(idFrontPhoto!!)
                imageUploading = "nationalIdFrontImage"
            }
        }


        /////////////////////
        binding.nationalIdBack.setOnClickListener {
            imageType  = "nationalIdBack"
            setUpImagePicker()
        }
        binding.deleteIdBack.setOnClickListener {
            binding.apply {
                imgIdBack.setImageURI(null)
                deleteIdBack.visibilityGone()
                uploadIdback.visibilityGone()
                nationalIdBack.enabled()
            }
            idBack = ""
        }
        binding.uploadIdback.setOnClickListener {
            if (idBackPhoto != null){
                uploadImage(idBackPhoto!!)
                imageUploading = "nationalIdBack"
            }
        }

        binding.submitAndContinuePersonal.setOnClickListener {
            if(!avatar.isNullOrBlank() && !idFront.isNullOrBlank() && !idBack.isNullOrBlank()){
                val mobile = args.mobile
                var isDarkMode = 0
                viewModel.registerCaptain(mobile,avatar,"deviceToken", "","android"
                ,idFront,idBack,
                    "captains/679d4821359d42ce14f4af0d75637fa9807d2c16.png",
                    "captains/679d4821359d42ce14f4af0d75637fa9807d2c16.png",isDarkMode)

            }
            else{
                showToast("upload All images")

            }
        }
        observeOnRegisterCaptain()
        observeOnUploadFile()
        }

    private fun observeOnRegisterCaptain() {

        lifecycleScope.launch {

         viewModel.registerState.collect{
                when(it?.status){
                    NetworkState.Status.SUCCESS->{
                        val data=it.data
                        val action=CreateAccountPersonalInfoFragmentDirections.actionCreateAccountPersonalInfoFragmentToCreateAccountVehicalInfoFragment()
                        findNavController().navigate(action)


                    }
                    NetworkState.Status.FAILED->{
                        showToast(it.msg.toString())
                        Log.d("Mostafa", it.msg.toString() )
                    }

                    else -> {
                        Unit
                    }
                }
            }
        }
    }

    private fun observeOnUploadFile() {
        lifecycleScope.launch {
         viewModel.mainEvent.collect{
                when(it?.status){
                    NetworkState.Status.SUCCESS->{
                        val data = it.data as Resource<UploadImageResponse>
                        val image = data.getData()?.data.toString()
                        imageUploaded(image)
                    }
                    NetworkState.Status.FAILED->{
                        showToast(it.msg.toString())
                        Log.d("Mostafa", it.msg.toString() )

                    }

                    else -> {
                        Unit
                    }
                }

            }
        }
    }

    private fun setUpImagePicker(){
        ImagePicker.with(this)
            .crop()
            .cameraOnly()
            .start()
    }
    private fun uploadImage(image: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            val a = decodeFile(image.path)
            val baos = ByteArrayOutputStream()
            a?.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val imageBytes: ByteArray = baos.toByteArray()

            val requestFile =
                imageBytes.toRequestBody("image/*".toMediaTypeOrNull(), 0, imageBytes.size)
            val body =
                MultipartBody.Part.createFormData("file", "image.jpg", requestFile)

            val name: RequestBody = Constants.VEHICLE_IMAGE_PATH
                .toRequestBody("text/plain".toMediaTypeOrNull())

            println("ADD_VEHICLE_UPLOAD_IMAGE  ${image.path}")
            viewModel.uploadImage(body, name)

        }

    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            when (imageType) {
                "PersonalPhoto" -> {
                    binding.apply {
                        imgPersonalPhoto.setImageURI(selectedImageUri)
                        deletePersonalPhoto.visibilityVisible()
                        uploadPersonalPhoto.visibilityVisible()
                        personalCaptainImage.disable()
                    }
                    personalPhoto = selectedImageUri
                }
                "nationalIdFrontImage" -> {
                    binding.apply {
                        imgIdFront.setImageURI(selectedImageUri)
                        deleteIdFront.visibilityVisible()
                        uploadIdFront.visibilityVisible()
                        nationalIdFrontImage.disable()
                    }
                    idFrontPhoto = selectedImageUri
                }
                "nationalIdBack" -> {
                    binding.apply {
                        imgIdBack.setImageURI(selectedImageUri)
                        deleteIdBack.visibilityVisible()
                        uploadIdback.visibilityVisible()
                        nationalIdBack.disable()
                    }
                    idBackPhoto = selectedImageUri
                }
            }
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error Error", Toast.LENGTH_SHORT).show()
        }

    }
    private fun imageUploaded(image:String) {
        when (imageUploading) {
            "PersonalPhoto" -> {
                avatar = image
                binding.apply {
                    uploadPersonalPhoto.visibilityGone()
                    deletePersonalPhoto.visibilityGone()
                    personalCaptainImage.disable()
                }
            }
            "nationalIdFrontImage" -> {
                idFront = image
                binding.apply {
                    uploadIdFront.visibilityGone()
                    deleteIdFront.visibilityGone()
                    nationalIdFrontImage.disable()
                }
            }
            "nationalIdBack" -> {
                idBack = image
                binding.apply {
                    uploadIdback.visibilityGone()
                    deleteIdBack.visibilityGone()
                    nationalIdBack.disable()
                }
            }
        }

    }

}