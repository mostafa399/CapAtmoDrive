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
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mostafahelal.atmodrive2.R
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.auth.data.utils.decodeFile
import com.mostafahelal.atmodrive2.auth.data.utils.Constants
import com.mostafahelal.atmodrive2.auth.data.utils.NetworkState
import com.mostafahelal.atmodrive2.auth.data.utils.Resource
import com.mostafahelal.atmodrive2.auth.data.utils.disable
import com.mostafahelal.atmodrive2.auth.data.utils.enabled
import com.mostafahelal.atmodrive2.auth.data.utils.showToast
import com.mostafahelal.atmodrive2.auth.data.utils.visibilityGone
import com.mostafahelal.atmodrive2.auth.data.utils.visibilityVisible
import com.mostafahelal.atmodrive2.auth.domain.model.FileUploadResponse
import com.mostafahelal.atmodrive2.auth.presentation.view_model.AuthViewModel
import com.mostafahelal.atmodrive2.databinding.FragmentCreateAccountVehicalInfoBinding
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
class CreateAccountVehicalInfoFragment : Fragment()  {
    private var _binding: FragmentCreateAccountVehicalInfoBinding?=null
    @Inject
    lateinit var sharedPreferences: ISharedPreferencesManager
    private val binding get() = _binding!!
    private val viewModel:AuthViewModel by viewModels()
    private var imageType: String = ""
    private var imageUploading = ""
    private var frontLicence=""
    private var backLicence=""
    private var frontLicencePhoto:Uri?=null
    private var backLicencePhoto:Uri?=null
    private var imagephoto1:Uri?=null
    private var image1=""
    private var image2=""
    private var image3=""
    private var image4=""
    private var image5=""
    private var image6=""
    private var imagephoto2:Uri?=null
    private var imagephoto3:Uri?=null
    private var imagephoto4:Uri?=null
    private var imagephoto5:Uri?=null
    private var imagephoto6:Uri?=null
    private lateinit var confirm:Button
    private lateinit var carImagesPB: ProgressBar
    private lateinit var  side1:ImageView
    private lateinit var  side2:ImageView
    private lateinit var  side3:ImageView
    private lateinit var  side4:ImageView
    private lateinit var  side5:ImageView
    private lateinit var  side6:ImageView
    private lateinit var uploadImage1:ImageView
    private lateinit var uploadImage2:ImageView
    private lateinit var uploadImage3:ImageView
    private lateinit var uploadImage4:ImageView
    private lateinit var uploadImage5:ImageView
    private lateinit var uploadImage6:ImageView
    private lateinit var deleteImage1:ImageView
    private lateinit var deleteImage2:ImageView
    private lateinit var deleteImage3:ImageView
    private lateinit var deleteImage4:ImageView
    private lateinit var deleteImage5:ImageView
    private lateinit var deleteImage6:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAccountVehicalInfoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentCreateAccountVehicalInfoBinding.bind(view)
        setBottomSheetDialog()
        setupToolbarNavigation()
        setupBackPressedHandler()

        side1.setOnClickListener {
            setUpImagePicker()
            imageType="side1"
        }
        side2.setOnClickListener {
            setUpImagePicker()
            imageType="side2"
        }
        side3.setOnClickListener {
            setUpImagePicker()
            imageType="side3"
        }
        side4.setOnClickListener {
            setUpImagePicker()
            imageType="side4"
        }
        side5.setOnClickListener {
            setUpImagePicker()
            imageType="side5"
        }
        side6.setOnClickListener {
            setUpImagePicker()
            imageType="side6"
        }
        deleteImage1.setOnClickListener {
            side1.setImageURI(null)
            binding.carImage1.setImageURI(null)
            deleteImage1.visibilityGone()
            uploadImage1.visibilityGone()
            side1.enabled()
            image1=""

        }
        uploadImage1.setOnClickListener {
            if (imagephoto1!=null){
                uploadImage(imagephoto1!!)
                imageUploading="side1"
                carImagesPB.visibilityVisible()
                blockUI(true)
            }
        }
        deleteImage2.setOnClickListener {
                    side2.setImageURI(null)
                    binding.carImage2.setImageURI(null)
                    deleteImage2.visibilityGone()
                    uploadImage2.visibilityGone()
                    side2.enabled()
                    image2=""

                }
        uploadImage2.setOnClickListener {
                    if (imagephoto2!=null){
                        uploadImage(imagephoto2!!)
                        imageUploading="side2"
                        carImagesPB.visibilityVisible()
                        blockUI(true)


                    }
                }
        deleteImage3.setOnClickListener {
                    side3.setImageURI(null)
                    binding.carImage3.setImageURI(null)
                    deleteImage3.visibilityGone()
                    uploadImage3.visibilityGone()
                    side3.enabled()
                    image3=""

                }
        uploadImage3.setOnClickListener {
                    if (imagephoto3!=null){
                        uploadImage(imagephoto3!!)
                        imageUploading="side3"
                        carImagesPB.visibilityVisible()
                        blockUI(true)


                    }
                }
        deleteImage4.setOnClickListener {
                    side4.setImageURI(null)
                    binding.carImage4.setImageURI(null)
                    deleteImage4.visibilityGone()
                    uploadImage4.visibilityGone()
                    side4.enabled()
                    image4=""

                }
        uploadImage4.setOnClickListener {
                    if (imagephoto4!=null){
                        uploadImage(imagephoto4!!)
                        imageUploading="side4"
                        carImagesPB.visibilityVisible()
                        blockUI(true)


                    }
                }
        deleteImage5.setOnClickListener {
                    side5.setImageURI(null)
                    binding.carImage5.setImageURI(null)
                    deleteImage5.visibilityGone()
                    uploadImage5.visibilityGone()
                    side5.enabled()
                    image5=""

                }
        uploadImage5.setOnClickListener {
                    if (imagephoto5!=null){
                        uploadImage(imagephoto5!!)
                        imageUploading="side5"
                        carImagesPB.visibilityVisible()
                        blockUI(true)


                    }
                }
        deleteImage6.setOnClickListener {
                    side6.setImageURI(null)
                    binding.carImage6.setImageURI(null)
                    deleteImage6.visibilityGone()
                    uploadImage6.visibilityGone()
                    side6.enabled()
                    image6=""

                }
        uploadImage6.setOnClickListener {
                    if (imagephoto6!=null){
                        uploadImage(imagephoto6!!)
                        imageUploading="side6"
                        carImagesPB.visibilityVisible()
                        blockUI(true)


                    }
                }

        binding.carFrontLicence.setOnClickListener {
            imageType="frontCarLicence"
            setUpImagePicker()
        }
        binding.deleteFrontCarLicenceImage.setOnClickListener {
            binding.apply {
                imgLicenseFront.setImageURI(null)
                deleteFrontCarLicenceImage.visibilityGone()
                doneFrontCarLicenceImage.visibilityGone()
                carFrontLicence.enabled()
            }
            frontLicence = ""
        }
        binding.doneFrontCarLicenceImage.setOnClickListener {
            if (frontLicencePhoto!=null){
                uploadImage(frontLicencePhoto!!)
                imageUploading="frontCarLicence"
                binding.vehicleInfoProgressBar.visibilityVisible()
               // blockUI(true)

            }
        }
        binding.backCarLicenceLayout.setOnClickListener {
            imageType="backCarLicence"
            setUpImagePicker()
        }
        binding.deleteBackCarLicenceImage.setOnClickListener {
            binding.apply {
                imgLicenseBack.setImageURI(null)
                deleteBackCarLicenceImage.visibilityGone()
                doneBackCarLicenceImage.visibilityGone()
                backCarLicenceLayout.enabled()
            }
            backLicence = ""
        }
        binding.doneBackCarLicenceImage.setOnClickListener {
            if (backLicencePhoto!=null){
                uploadImage(backLicencePhoto!!)
                imageUploading="backCarLicence"
                binding.vehicleInfoProgressBar.visibilityVisible()
               // blockUI(true)

            }
        }
        observeOnUploadFile()
        binding.submitAndContinueVehical.setOnClickListener {
            if (frontLicence.isNotBlank() && backLicence.isNotBlank() && image1.isNotBlank()
                && image2.isNotBlank()&& image3.isNotBlank()&& image4.isNotBlank()&& image5.isNotBlank()
                && image6.isNotBlank()){
                viewModel.registerVehical(image1,image2,image3,image4,image5,image6,frontLicence,backLicence)
            }else{
                showToast("Add all images")
            }
        }
        observeOnRegisterVehicalInfo()
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
    private fun observeOnUploadFile() {
        lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.mainEvent.collect {netWorkState->
                try {
                    when (netWorkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            val data = netWorkState.data as Resource<FileUploadResponse>
                            blockUI(false)
                            binding.vehicleInfoProgressBar.visibilityGone()
                            carImagesPB.visibilityGone()
                            val image = data.data?.data.toString()
                            imageUploaded(image)
                        }
                        NetworkState.Status.FAILED -> {

                            blockUI(false)
                            showToast(netWorkState.msg.toString())
                            Log.d("Mostafa", netWorkState.msg.toString())
                            binding.vehicleInfoProgressBar.visibilityGone()
                            carImagesPB.visibilityGone()

                        }
                        NetworkState.Status.RUNNING ->{

                            blockUI(true)
                            binding.vehicleInfoProgressBar.visibilityVisible()
                            carImagesPB.visibilityVisible()


                        }
                        else -> {
                            Unit
                        }
                    }
                }catch (e:Exception){
                    e.localizedMessage
                }

            }
        }
        }
    }
    fun observeOnRegisterVehicalInfo(){
        lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.registerVehicalState.collect{
                when(it?.status){
                    NetworkState.Status.SUCCESS->{
                        val action=CreateAccountVehicalInfoFragmentDirections.actionCreateAccountVehicalInfoFragmentToCreateAccountBankInfoFragment()
                        findNavController().navigate(action)
                        binding.vehicleInfoProgressBar.visibilityGone()
                    }
                    NetworkState.Status.FAILED->{

                        showToast(it.msg.toString())
                        binding.vehicleInfoProgressBar.visibilityGone()

                    }
                    NetworkState.Status.RUNNING ->{
                        binding.vehicleInfoProgressBar.visibilityVisible()
                    }

                    else -> {
                        Unit
                    }

                }
            }
        }
        }
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            when (imageType) {
                "frontCarLicence" -> {
                        binding.imgLicenseFront.setImageURI(selectedImageUri)
                        binding.deleteFrontCarLicenceImage.visibilityVisible()
                        binding.doneFrontCarLicenceImage.visibilityVisible()
                        binding.carFrontLicence.disable()
                        frontLicencePhoto = selectedImageUri
                }
                "backCarLicence" -> {

                        binding.imgLicenseBack.setImageURI(selectedImageUri)
                        binding.deleteBackCarLicenceImage.visibilityVisible()
                        binding.doneBackCarLicenceImage.visibilityVisible()
                        binding.backCarLicenceLayout.disable()
                        backLicencePhoto = selectedImageUri
                }
                "side1"->{
                    side1.setImageURI(selectedImageUri)
                    side1.disable()
                    deleteImage1.visibilityVisible()
                    uploadImage1.visibilityVisible()
                    binding.carImage1.setImageURI(selectedImageUri)
                    imagephoto1 = selectedImageUri
                }
                "side2"->{
                    side2.setImageURI(selectedImageUri)
                    side2.disable()
                    deleteImage2.visibilityVisible()
                    uploadImage2.visibilityVisible()
                    binding.carImage2.setImageURI(selectedImageUri)
                    imagephoto2 = selectedImageUri

                }
                "side3"->{
                    side3.setImageURI(selectedImageUri)
                    side3.disable()
                    deleteImage3.visibilityVisible()
                    uploadImage3.visibilityVisible()
                    binding.carImage3.setImageURI(selectedImageUri)
                    imagephoto3 = selectedImageUri

                }
                "side4"->{
                    side4.setImageURI(selectedImageUri)
                    side4.disable()
                    deleteImage4.visibilityVisible()
                    uploadImage4.visibilityVisible()
                    binding.carImage4.setImageURI(selectedImageUri)
                    imagephoto4 = selectedImageUri

                }
                "side5"->{
                    side5.setImageURI(selectedImageUri)
                    side5.disable()
                    deleteImage5.visibilityVisible()
                    uploadImage5.visibilityVisible()
                    binding.carImage5.setImageURI(selectedImageUri)
                    imagephoto5 = selectedImageUri

                }
                "side6"->{
                    side6.setImageURI(selectedImageUri)
                    side6.disable()
                    deleteImage6.visibilityVisible()
                    uploadImage6.visibilityVisible()
                    binding.carImage6.setImageURI(selectedImageUri)
                    imagephoto6 = selectedImageUri

                }
            }

        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error Error", Toast.LENGTH_SHORT).show()
        }

    }
    private fun imageUploaded(image: String) {
        when (imageUploading){
            "frontCarLicence"->{
                frontLicence=image
                binding.apply {
                    doneFrontCarLicenceImage.visibilityGone()
                    deleteFrontCarLicenceImage.visibilityVisible()
                    carFrontLicence.disable()
                }

            }
            "backCarLicence"->{
                backLicence=image
                binding.apply {
                    doneBackCarLicenceImage.visibilityGone()
                    deleteBackCarLicenceImage.visibilityVisible()
                    backCarLicenceLayout.disable()
                }

            }
            "side1"->{
                image1=image
                    uploadImage1.visibilityGone()
                    deleteImage1.visibilityVisible()
                    side1.disable()
            }
            "side2"->{
                image2=image
                    uploadImage2.visibilityGone()
                    deleteImage2.visibilityVisible()
                    side2.disable()
            }
            "side3"->{
                image3=image
                    uploadImage3.visibilityGone()
                    deleteImage3.visibilityVisible()
                    side3.disable()
            }
            "side4"->{
                image4=image
                    uploadImage4.visibilityGone()
                    deleteImage4.visibilityVisible()
                    side4.disable()
            }
            "side5"->{
                image5=image
                    uploadImage5.visibilityGone()
                    deleteImage5.visibilityVisible()
                    side5.disable()
            }
            "side6"->{
                image6=image
                    uploadImage6.visibilityGone()
                    deleteImage6.visibilityVisible()
                    side6.disable()
            }
        }
    }

    private fun setBottomSheetDialog(){
          val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.item_bottom_sheet)
         side1 = bottomSheetDialog.findViewById(R.id.firstImage)!!
         side2 = bottomSheetDialog.findViewById(R.id.SecondImage)!!
         side3 = bottomSheetDialog.findViewById(R.id.thirdImage)!!
         side4 = bottomSheetDialog.findViewById(R.id.fourthImage)!!
         side5 = bottomSheetDialog.findViewById(R.id.fifthImage)!!
         side6 = bottomSheetDialog.findViewById(R.id.sixImage)!!
         uploadImage1 = bottomSheetDialog.findViewById(R.id.addimage1)!!
         uploadImage2 = bottomSheetDialog.findViewById(R.id.addimage2)!!
         uploadImage3 = bottomSheetDialog.findViewById(R.id.addimage3)!!
         uploadImage4 = bottomSheetDialog.findViewById(R.id.addimage4)!!
         uploadImage5 = bottomSheetDialog.findViewById(R.id.addimage5)!!
         uploadImage6 = bottomSheetDialog.findViewById(R.id.addimage6)!!
        deleteImage1 = bottomSheetDialog.findViewById(R.id.deleteimage1)!!
        deleteImage2 = bottomSheetDialog.findViewById(R.id.deleteimage2)!!
        deleteImage3 = bottomSheetDialog.findViewById(R.id.deleteimage3)!!
        deleteImage4 = bottomSheetDialog.findViewById(R.id.deleteimage4)!!
        deleteImage5 = bottomSheetDialog.findViewById(R.id.deleteimage5)!!
        deleteImage6 = bottomSheetDialog.findViewById(R.id.deleteimage6)!!
        confirm = bottomSheetDialog.findViewById(R.id.next2_btn)!!
        carImagesPB = bottomSheetDialog.findViewById(R.id.car_images_progressBar)!!

        confirm.setOnClickListener {
            bottomSheetDialog.dismiss()
            binding.VehicalText.visibilityGone()
            binding.CarImage.visibilityGone()
        }
        binding.carImageLay.setOnClickListener {
            bottomSheetDialog.show()
        }
    }
    private fun blockUI(blocked: Boolean){
        if (blocked){

                side1.disable()
                side2.disable()
                side3.disable()
                side4.disable()
                side5.disable()
                side6.disable()
                uploadImage1.disable()
                uploadImage2.disable()
                uploadImage3.disable()
                uploadImage4.disable()
                uploadImage5.disable()
                uploadImage6.disable()
                deleteImage1.disable()
                deleteImage2.disable()
                deleteImage3.disable()
                deleteImage4.disable()
                deleteImage5.disable()
                deleteImage6.disable()
                confirm.disable()
                binding.CarImage.disable()
                binding.deleteImage6Images.disable()
                binding.carFrontLicence.disable()
                binding.backCarLicenceLayout.disable()
                binding.deleteBackCarLicenceImage.disable()
                binding.deleteFrontCarLicenceImage.disable()
                binding.doneFrontCarLicenceImage.disable()
                binding.doneFrontCarLicenceImage.disable()


        }else{
            binding.apply {
                side1.enabled()
                side2.enabled()
                side3.enabled()
                side4.enabled()
                side5.enabled()
                side6.enabled()
                uploadImage1.enabled()
                uploadImage2.enabled()
                uploadImage3.enabled()
                uploadImage4.enabled()
                uploadImage5.enabled()
                uploadImage6.enabled()
                deleteImage1.enabled()
                deleteImage2.enabled()
                deleteImage3.enabled()
                deleteImage4.enabled()
                deleteImage5.enabled()
                deleteImage6.enabled()
                confirm.enabled()
                binding.CarImage.enabled()
                binding.deleteImage6Images.enabled()
                binding.carFrontLicence.enabled()
                binding.backCarLicenceLayout.enabled()
                binding.deleteBackCarLicenceImage.enabled()
                binding.deleteFrontCarLicenceImage.enabled()
                binding.doneFrontCarLicenceImage.enabled()
                binding.doneFrontCarLicenceImage.enabled()

            }
        }
    }

    private fun setupBackPressedHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               requireActivity().finish()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    private fun setupToolbarNavigation() {
        binding.topappbar.setNavigationOnClickListener {
          requireActivity().finish()


        }
    }


}