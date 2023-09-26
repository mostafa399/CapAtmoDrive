package com.mostafahelal.atmodrive2.auth.presentation.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.auth.data.utils.Constants
import com.mostafahelal.atmodrive2.auth.data.utils.NetworkState
import com.mostafahelal.atmodrive2.auth.data.utils.Resource
import com.mostafahelal.atmodrive2.auth.data.utils.decodeFile
import com.mostafahelal.atmodrive2.auth.data.utils.disable
import com.mostafahelal.atmodrive2.auth.data.utils.enabled
import com.mostafahelal.atmodrive2.auth.data.utils.showToast
import com.mostafahelal.atmodrive2.auth.data.utils.visibilityGone
import com.mostafahelal.atmodrive2.auth.data.utils.visibilityVisible
import com.mostafahelal.atmodrive2.auth.domain.model.FileUploadResponse
import com.mostafahelal.atmodrive2.auth.presentation.view_model.AuthViewModel
import com.mostafahelal.atmodrive2.databinding.FragmentCreateAccountPersonalInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CreateAccountPersonalInfoFragment : Fragment() {
    @Inject
    lateinit var sharedPreferences: ISharedPreferencesManager
    private var imageType = ""
    private var imageUploading = ""
    private var avatar = ""
    private var idFront = ""
    private var idBack = ""
    private var LicenceFront = ""
    private var LicenceBack = ""
    private var personalPhoto:Uri? = null
    private var idFrontPhoto:Uri? = null
    private var idBackPhoto:Uri? = null
    private var LicenceFrontPhoto:Uri? = null
    private var LicenceBackPhoto:Uri? = null
    private val viewModel :AuthViewModel by viewModels()
    private lateinit var binding: FragmentCreateAccountPersonalInfoBinding
    private val args by navArgs<CreateAccountPersonalInfoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentCreateAccountPersonalInfoBinding.inflate(layoutInflater)
        return binding.root
       }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentCreateAccountPersonalInfoBinding.bind(view)
        setupBackPressedHandler()
        setupToolbarNavigation()
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
                binding.personalInfoProgressBar.visibilityVisible()
                blockUI(true)

            }
        }
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
                binding.personalInfoProgressBar.visibilityVisible()
                blockUI(true)


            }
        }

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
                binding.personalInfoProgressBar.visibilityVisible()
                blockUI(true)
            }
        }


        binding.drivingLicenseFrontLayout.setOnClickListener {
                imageType  = "drivingLicenseFrontLayout"
                setUpImagePicker()
            }
        binding.deletelicenceFront.setOnClickListener {
            binding.apply {
                drivingLicenseFront.setImageURI(null)
                deletelicenceFront.visibilityGone()
                uploadLicenceFront.visibilityGone()
                drivingLicenseFrontLayout.enabled()
            }
            LicenceFront = ""
        }
        binding.uploadLicenceFront.setOnClickListener {
            if (LicenceFrontPhoto != null){
                uploadImage(LicenceFrontPhoto!!)
                imageUploading = "drivingLicenseFrontLayout"
                binding.personalInfoProgressBar.visibilityVisible()
                blockUI(true)
            }
        }

        binding.drivingLicenseBackLayout.setOnClickListener {
                imageType  = "drivingLicenseBackLayout"
                setUpImagePicker()
            }
        binding.deleteLicenceBack.setOnClickListener {
            binding.apply {
                drivingLicenseBack.setImageURI(null)
                deleteLicenceBack.visibilityGone()
                uploadLicenceback.visibilityGone()
                drivingLicenseBackLayout.enabled()
            }
            LicenceBack = ""
        }
        binding.uploadLicenceback.setOnClickListener {
            if (LicenceBackPhoto != null){
                uploadImage(LicenceBackPhoto!!)
                imageUploading = "drivingLicenseBackLayout"
                binding.personalInfoProgressBar.visibilityVisible()
                blockUI(true)
            }
        }

        binding.submitAndContinuePersonal.setOnClickListener {
            if(avatar.isNotBlank() && idFront.isNotBlank() && idBack.isNotBlank()&&LicenceFront.isNotBlank()&&LicenceBack.isNotBlank()){
                val mobile = args.mobile
                var isDarkMode = 0
                viewModel.registerCaptain(mobile,avatar,"deviceToken", "device_id","android"
                ,idFront,idBack, LicenceFront, LicenceBack,isDarkMode)
            }
            else{
                showToast("upload All images")
            }
        }
        observeOnRegisterCaptain()
        onUploadFile()
        }


        private fun observeOnRegisterCaptain() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED){
         viewModel.registerState.collect{
                when(it?.status){
                    NetworkState.Status.SUCCESS->{
                        withContext(Dispatchers.Main){
                        val action=CreateAccountPersonalInfoFragmentDirections.actionCreateAccountPersonalInfoFragmentToCreateAccountVehicalInfoFragment()
                        findNavController().navigate(action)
                        binding.personalInfoProgressBar.visibilityGone()
                        }
                    }
                    NetworkState.Status.FAILED->{
                        withContext(Dispatchers.Main){
                        showToast(it.msg.toString())
                        binding.personalInfoProgressBar.visibilityGone()
                    }
                    }
                    NetworkState.Status.RUNNING ->{
                        withContext(Dispatchers.Main){
                        binding.personalInfoProgressBar.visibilityVisible()
                    }
                    }

                    else -> {
                        Unit
                    }
                }
            }
        }
        }
    }
          private fun onUploadFile() {
           viewLifecycleOwner.lifecycleScope.launch {
               repeatOnLifecycle(Lifecycle.State.STARTED) {
                   viewModel.mainEvent.collect { networkState ->
                       when (networkState?.status) {
                           NetworkState.Status.SUCCESS -> {
                               blockUI(false)
                               binding.personalInfoProgressBar.visibilityGone()
                               val data = networkState.data as Resource<FileUploadResponse>
                               val image = data.data?.data.toString()
                               imageUploaded(image)
                           }

                           NetworkState.Status.FAILED -> {
                               blockUI(false)
                               showToast(networkState.msg.toString())
                               Log.d("Mostafa", networkState.msg.toString())
                               binding.personalInfoProgressBar.visibilityGone()
                           }

                           NetworkState.Status.RUNNING -> {
                               blockUI(true)
                               binding.personalInfoProgressBar.visibilityVisible()
                           }

                           else -> {
                               Unit
                           }
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
       viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

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

            println("uploadImage  ${image.path}")

            viewModel.uploadImage(part = body, path = name)

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
                "drivingLicenseFrontLayout" -> {
                    binding.apply {
                        drivingLicenseFront.setImageURI(selectedImageUri)
                        deletelicenceFront.visibilityVisible()
                        uploadLicenceFront.visibilityVisible()
                        drivingLicenseFrontLayout.disable()
                    }
                    LicenceFrontPhoto = selectedImageUri
                }
                "drivingLicenseBackLayout" -> {
                    binding.apply {
                        drivingLicenseBack.setImageURI(selectedImageUri)
                        deleteLicenceBack.visibilityVisible()
                        uploadLicenceback.visibilityVisible()
                        drivingLicenseBackLayout.disable()
                    }
                    LicenceBackPhoto = selectedImageUri
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
                    deletePersonalPhoto.visibilityVisible()
                    personalCaptainImage.disable()
                }
            }
            "nationalIdFrontImage" -> {
                idFront = image
                binding.apply {
                    uploadIdFront.visibilityGone()
                    deleteIdFront.visibilityVisible()
                    nationalIdFrontImage.disable()
                }
            }
            "nationalIdBack" -> {
                idBack = image
                binding.apply {
                    uploadIdback.visibilityGone()
                    deleteIdBack.visibilityVisible()
                    nationalIdBack.disable()
                }
            }
            "drivingLicenseFrontLayout" -> {
                LicenceFront = image
                binding.apply {
                    uploadLicenceFront.visibilityGone()
                    deletelicenceFront.visibilityVisible()
                    drivingLicenseFrontLayout.disable()
                }
            }
            "drivingLicenseBackLayout" -> {
                LicenceBack = image
                binding.apply {
                    uploadLicenceback.visibilityGone()
                    deleteLicenceBack.visibilityVisible()
                    drivingLicenseBackLayout.disable()
                }
            }
        }
    }
    private fun blockUI(blocked: Boolean){
        if (blocked){
            binding.apply {
                personalCaptainImage.disable()
                nationalIdFrontImage.disable()
                nationalIdBack.disable()
                deletePersonalPhoto.disable()
                deleteIdFront.disable()
                deleteIdBack.disable()
                uploadPersonalPhoto.disable()
                uploadIdFront.disable()
                uploadIdback.disable()
                drivingLicenseBackLayout.disable()
                deleteLicenceBack.disable()
                uploadLicenceback.disable()
                drivingLicenseFrontLayout.disable()
                deletelicenceFront.disable()
                uploadLicenceFront.disable()
                submitAndContinuePersonal.disable()
            }
        }else{
            binding.apply {
                personalCaptainImage.enabled()
                nationalIdFrontImage.enabled()
                nationalIdBack.enabled()
                deletePersonalPhoto.enabled()
                deleteIdFront.enabled()
                deleteIdBack.enabled()
                uploadPersonalPhoto.enabled()
                uploadIdFront.enabled()
                uploadIdback.enabled()
                drivingLicenseBackLayout.enabled()
                deleteLicenceBack.enabled()
                uploadLicenceback.enabled()
                drivingLicenseFrontLayout.enabled()
                deletelicenceFront.enabled()
                uploadLicenceFront.enabled()
                submitAndContinuePersonal.enabled()
            }
        }
    }

    private fun setupBackPressedHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                sharedPreferences.clearString(Constants.REMEMBER_TOKEN_PREFS)
                sharedPreferences.clearString(Constants.REGISTER_STEP_PREFS)

        }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    private fun setupToolbarNavigation() {
        binding.topappbar.setNavigationOnClickListener {
            findNavController().popBackStack()
            sharedPreferences.clearString(Constants.REMEMBER_TOKEN_PREFS)
            sharedPreferences.clearString(Constants.REGISTER_STEP_PREFS)

        }
    }

}