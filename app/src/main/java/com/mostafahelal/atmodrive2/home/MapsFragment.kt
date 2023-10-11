package com.mostafahelal.atmodrive2.home

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mostafahelal.atmodrive2.R
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.databinding.FragmentMapsBinding
import com.mostafahelal.atmodrive2.home.model.CaptainModel
import com.mostafahelal.atmodrive2.utils.Constants
import com.mostafahelal.atmodrive2.utils.visibilityGone
import com.mostafahelal.atmodrive2.utils.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject
@AndroidEntryPoint
class MapsFragment : Fragment(),OnMapReadyCallback {
    @Inject
     lateinit var preferencesManager: ISharedPreferencesManager
    private lateinit var binding:FragmentMapsBinding
    var Phone:String=""
    private lateinit var mMap: GoogleMap
    private var mapFragment:SupportMapFragment?=null
    private var mLocationRequest : LocationRequest?= null
    private var mLocationCallback : LocationCallback ?= null
    private var mFusedLocationClient : FusedLocationProviderClient?= null
    lateinit var latLng :LatLng
    val mapLacation = HashMap<String,Any>()
    val list = ArrayList<CaptainModel>()
    private var mBackPressed: Long = 0
    private var movingCabMarker : Marker?= null
    private var previousLatLng: LatLng? = null
    private var currentLatLng: LatLng? = null
    private var valueAnimator: ValueAnimator? = null
    private val ref = FirebaseDatabase.getInstance().getReference(Constants.ONLINE_CAPTAINS)
    private var valueEventListener:ValueEventListener?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMapsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentMapsBinding.bind(view)
        Phone=preferencesManager.getString(Constants.MOBILE_PREFS)
        valueEventListener =  object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val tripId=snapshot.getValue(String::class.java)
//                Log.e("TAG", "onDataChange: $tripId", )
                if (tripId!="0" && binding.checkBox.isChecked){
                    //show buttom Sheet
                binding.tripCycleCard.visibilityVisible()

                }else  binding.tripCycleCard.visibilityGone()

     /*  val captain = snapshot.getValue(CaptainModel::class.java)

                for (snap1 in snapshot.children) {
                    val captains = snap1.getValue(CaptainModel::class.java)
                    list.add(captains!!)

                }*/

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
     //   ref.addValueEventListener(valueEventListener!!)

        ref.child(Phone).child("tripId")
            .addValueEventListener(valueEventListener!!)

        binding.checkBox.setOnCheckedChangeListener{buttonView, isChecked ->
            if (isChecked) {
                uploadCaptainToFirebase()
            } else {
                removeCaptainFromFirebase()
            }

        }
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initLocation()
    }

    private fun removeCaptainFromFirebase() {
        ref.child(Phone).setValue(null)
        binding.checkButtonLayout.setBackgroundResource(R.drawable.back_checkbox_lay)
        binding.textView6.setText("You are offline")

    }

    private fun uploadCaptainToFirebase() {
        ref.child(Phone).setValue(CaptainModel(Phone,"0","${mapLacation["lat"]}","${mapLacation["lng"]}"))
        binding.checkButtonLayout.setBackgroundResource(R.drawable.into_button_back)
        binding.textView6.setText("You are online")


    }

    private fun initLocation (){
        if (mFusedLocationClient == null){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        }

        if (mLocationRequest == null){
            mLocationRequest = LocationRequest.create()
                .setPriority( Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000)
                .setFastestInterval(2000)
                .setSmallestDisplacement(5f)
        }

    }
    @SuppressLint("MissingPermission")
    private fun getLocation(){
       // mMap.isMyLocationEnabled=true
        //mMap.uiSettings.isMyLocationButtonEnabled=false
        mMap.uiSettings.isCompassEnabled=false
        mLocationCallback = object :LocationCallback(){
            override fun onLocationResult(result: LocationResult) {
                latLng = LatLng(result.lastLocation!!.latitude,result.lastLocation!!.longitude)
                mapLacation["lat"] = result.lastLocation!!.latitude.toString()
                mapLacation["lng"] = result.lastLocation!!.longitude.toString()
                if(binding.checkBox.isChecked){
                    ref.child(Phone).updateChildren(mapLacation)
                }
                updateCarLocation(latLng)

            }
        }

        mFusedLocationClient?.requestLocationUpdates(mLocationRequest!!,mLocationCallback!!, Looper.getMainLooper())


    }
    private fun moveCameraMap (latLng: LatLng){
        val cameraPos = CameraPosition.builder()
            .target(latLng).zoom(18f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos))
    }
    private fun locationChecker(){

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest!!)

        val result : Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(requireActivity())
                .checkLocationSettings(builder.build())

        result.addOnCompleteListener {task->

            try {
                task.getResult(ApiException::class.java)

                getLocation()
            }catch (exception : ApiException){

                when(exception.statusCode){

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->{
                        try {

                            val resolve = exception as ResolvableApiException
                            startIntentSenderForResult(resolve.resolution.intentSender
                                ,Priority.PRIORITY_HIGH_ACCURACY
                                ,null,0,

                                0,0,null)

                        }catch (ex : Exception){}


                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE->{

                    }

                }
            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Priority.PRIORITY_HIGH_ACCURACY ->{
                when(resultCode){
                    Activity.RESULT_OK ->{
                        getLocation()
                    }
                    Activity.RESULT_CANCELED ->{
                        locationChecker()
                    }
                }
            }
        }
    }
    private fun checkPermission (){
        if (ActivityCompat
                .checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED  && ActivityCompat
                .checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(requireActivity()
                , arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION),2)
        }else{
            locationChecker()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 2 && permissions.isNotEmpty()
            &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
            locationChecker()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkPermission()
        mMap.setOnCameraIdleListener {
           // dropOff = mMap.cameraPosition.target
        }
    }



    private fun addCarMarkerAndGet(latLng: LatLng): Marker {
        val bitmapDescriptor =
            BitmapDescriptorFactory.fromBitmap(MapUtils.getCarBitmap(requireContext()))
        return mMap.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )!!
    }




    private fun updateCarLocation(latLng: LatLng) {
        val a = System.currentTimeMillis() - mBackPressed
        if (a < 2600) {
            return
        }
        mBackPressed = System.currentTimeMillis()

        //one Instance of marker
        if (movingCabMarker == null) {
            movingCabMarker = addCarMarkerAndGet(latLng)
        }
        // setting marker rotation  using previous and current location
        if (previousLatLng == null) {
            currentLatLng = latLng
            previousLatLng = currentLatLng
            movingCabMarker?.position = currentLatLng!!
            movingCabMarker?.setAnchor(0.5f, 0.5f)
            animateCameraInTrip(currentLatLng!!, previousLatLng!!)
        } else {
            // animateCameraInTrip(currentLatLng!!, previousLatLng!!)
            previousLatLng = currentLatLng

            animateCameraInTrip(latLng, previousLatLng!!)
            valueAnimator = AnimationUtils.carAnimator()

            valueAnimator?.addUpdateListener { va ->
                currentLatLng = latLng
                if (currentLatLng != null && previousLatLng != null) {

                    val multiplier = va.animatedFraction
                    val nextLocation = LatLng(
                        multiplier * currentLatLng!!.latitude + (1 - multiplier) * previousLatLng!!.latitude,
                        multiplier * currentLatLng!!.longitude + (1 - multiplier) * previousLatLng!!.longitude
                    )
                    movingCabMarker?.position = nextLocation
                    val rotation = MapUtils.getRotation(previousLatLng!!, nextLocation)
                    if (!rotation.isNaN()) {
                        movingCabMarker?.rotation = rotation
                    }
                    movingCabMarker?.setAnchor(0.5f, 0.5f)
                }
            }
            valueAnimator?.start()
        }

    }
    private fun animateCameraInTrip(latLng: LatLng, previous: LatLng) {

        val cameraPosition = CameraPosition.Builder()
            .bearing(LocationHelper.getBearing(previous, latLng))
            .target(latLng).tilt(45f).zoom(18f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        ref.child(Phone)
            .child("tripId")
            .removeEventListener(valueEventListener!!)
    }

}