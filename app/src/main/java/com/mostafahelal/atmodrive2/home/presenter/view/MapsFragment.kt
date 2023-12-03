package com.mostafahelal.atmodrive2.home.presenter.view

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mostafahelal.atmodrive2.R
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.databinding.FragmentMapsBinding
import com.mostafahelal.atmodrive2.utils.AnimationUtils
import com.mostafahelal.atmodrive2.utils.LocationHelper
import com.mostafahelal.atmodrive2.utils.MapUtils
import com.mostafahelal.atmodrive2.home.domain.model.PassengerData
import com.mostafahelal.atmodrive2.home.domain.model.UpdateAvailability
import com.mostafahelal.atmodrive2.home.presenter.viewmodel.SharedViewModel
import com.mostafahelal.atmodrive2.home.presenter.viewmodel.TripViewModel
import com.mostafahelal.atmodrive2.utils.Constants
import com.mostafahelal.atmodrive2.utils.NetworkState
import com.mostafahelal.atmodrive2.utils.Resource
import com.mostafahelal.atmodrive2.utils.getData
import com.mostafahelal.atmodrive2.utils.showToast
import com.mostafahelal.atmodrive2.utils.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
@AndroidEntryPoint
class MapsFragment : Fragment(),OnMapReadyCallback {
    @Inject
    lateinit var preferencesManager: ISharedPreferencesManager
    private lateinit var binding:FragmentMapsBinding
    val sharedViewModel:SharedViewModel by activityViewModels()
    private val tripViewModel :TripViewModel by viewModels()
    private lateinit var mMap: GoogleMap

    private var mapFragment:SupportMapFragment?=null
    private var mLocationRequest : LocationRequest?= null
    private var mLocationCallback : LocationCallback ?= null
    private var mFusedLocationClient : FusedLocationProviderClient?= null
    private var mBackPressed: Long = 0

    private var movingCabMarker : Marker?= null
    private var previousLatLng: LatLng? = null
    private var currentLatLng: LatLng? = null
    private var valueAnimator: ValueAnimator? = null
    private var bottomSheetView: ConstraintLayout? = null
    private var sheet= BottomSheetBehavior<ConstraintLayout>()
    private var myNavHostFragment: NavHostFragment? = null
    //save lat && lng
    val mapLocation = HashMap<String,Any>()
    //checkAvailability
    var online = false
    var tripAccepted = false
    private var captainId = ""
    var tripId = 0
    private var status = ""
    private var pickUpMarker : Marker?= null
    private var dropOffMarker : Marker?= null
    private lateinit var db: DatabaseReference
    private var valueEventListener : ValueEventListener?= null
    private var valueEventListenerOnTrip : ValueEventListener?= null


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

        init()
        initLocation()
        initBottomSheets()
        handleBottomSheetSize()
        onBackPressHandle()
        onClick()
        observeOnRequestStatus()
        observeOnAcceptReject()
        observer()
        updateCaptainStatus()
    }

    fun init(){
        myNavHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment11) as NavHostFragment
        db = Firebase.database.reference
        captainId=preferencesManager.getString(Constants.CAPTAIN_ID)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }
    private fun listenerOnTripId()  {
        valueEventListener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val id = snapshot.getValue(Int::class.java)

                if (id != null){
                    if (id != 0){
                        tripId = id
                        sharedViewModel.setTripId(id)
                        if(!tripAccepted){
                            showBottomSheet(R.navigation.bottom_nav_gragh)
                        }
                    }else{
                        clearMap()
                    }
                    preferencesManager.saveBoolean(Constants.CAPTAIN_STATUS,true)
                    updateCaptainStatus()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        db.child(Constants.ONLINE_CAPTAINS).child(captainId).child("tripId")
            .addValueEventListener(valueEventListener!!)
    }

    private fun updateCaptainStatus(){
        val capStatus = preferencesManager.getBoolean(Constants.CAPTAIN_STATUS)
        if (!capStatus){
            binding.textView6.apply {
                online = false
                text = "You are offline"
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            binding.checkButtonLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.error))
            binding.checkBox.isChecked = false

        }else {

            binding.textView6.apply {
                online = true
                text = "You are online"
                setTextColor(ContextCompat.getColor(requireContext(), R.color.Title))
            }
            binding.checkButtonLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Background))
            binding.checkBox.isChecked = true


        }
    }
    fun onClick(){
        binding.checkBox.setOnClickListener {
            if (mapLocation.isNotEmpty())
                tripViewModel.updateAvailability(mapLocation["lat"].toString(),mapLocation["lng"].toString())
        }

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
        mLocationCallback = object : LocationCallback(){
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)

                val latLng = result.lastLocation!!
                val lat = latLng.latitude.toString()
                val lng = latLng.longitude.toString()
                mapLocation["lat"] = lat
                mapLocation["lng"] = lng
                updateCarLocation(LatLng(latLng.latitude,latLng.longitude))
                Constants.captainLatLng = LatLng(latLng.latitude,latLng.longitude)

                if (online){
                    db.child(Constants.ONLINE_CAPTAINS).child(captainId).updateChildren(mapLocation)
                    if (tripAccepted){
                        db.child(Constants.TRIPS).child(tripId.toString()).updateChildren(mapLocation)
                    }
                }

            }
        }

        mFusedLocationClient?.requestLocationUpdates(mLocationRequest!!,mLocationCallback!!, Looper.getMainLooper())

    }

    private fun locationChecker(){

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest!!)

        val result : Task<LocationSettingsResponse> =
                 LocationServices
                .getSettingsClient(requireActivity())
                .checkLocationSettings(builder.build())

        result.addOnCompleteListener {task->

            try {
                task.getResult(ApiException::class.java)
                getLocation()
                tripViewModel.onTrip()
                listenerOnTripId()
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
                        showToast("Location Error")

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
                        tripViewModel.onTrip()
                        listenerOnTripId()
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


    private fun initBottomSheets() {
        bottomSheetView = binding.tripBottomSheets.tripBottomSheet
        sheet = BottomSheetBehavior.from(bottomSheetView!!)
        sheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == 4) {
                    Constants.isBottomSheetOn = false
                } else if (newState == 2) {
                    Constants.isBottomSheetOn = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })
    }
    private fun handleBottomSheetSize() {

        myNavHostFragment?.navController?.addOnDestinationChangedListener { _, destination, arguments ->
            if (destination.id == R.id.bottomSheetFinshedTripFragment ||destination.id == R.id.bottomSheetNewTripFragment||destination.id == R.id.botttomSheetAcceptedTripFragment) {
                sheet.isDraggable = false
            }
            else {
                sheet.isDraggable = false
            }

        }
    }
    private fun onBackPressHandle() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val childFragment = myNavHostFragment?.childFragmentManager?.fragments

            if (childFragment?.size != 0 && Constants.isBottomSheetOn) {
                var fragment = childFragment?.get(0)

                if ((fragment is BotttomSheetAcceptedTripFragment)
                    && sheet.state == BottomSheetBehavior.STATE_EXPANDED
                ) {
                    if (fragment is BotttomSheetAcceptedTripFragment) {
                        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            } else {
                requireActivity().finish()
            }
        }
    }
    private fun showBottomSheet(nav:Int) {
        val inflater = myNavHostFragment?.navController?.navInflater
        val graph = inflater?.inflate(nav)
        myNavHostFragment?.navController?.graph = graph!!
        sheet.state = BottomSheetBehavior.STATE_EXPANDED
        Constants.isBottomSheetOn = true

    }

    private fun observeOnRequestStatus(){
        sharedViewModel.requestStatus.observe(requireActivity(), Observer {

            if (it){
                // trip accepted
                tripAccepted = true
                showBottomSheet(R.navigation.nav_trip)
                listenerOnTrip()
            }else{
                clearMap()
            }

        })
    }
    private fun observeOnAcceptReject(){
        sharedViewModel.tripStatus.observe(requireActivity(), Observer {

            if (false){
                sheet.state = BottomSheetBehavior.STATE_COLLAPSED
            }


        })
    }
    private fun listenerOnTrip(){
        valueEventListenerOnTrip =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val tripStatus = snapshot.getValue(String::class.java)

                if(tripStatus != null){
                    status = tripStatus
                    when (status){
                        "accepted" -> {
                            pickUpPassengerMarker()
                        }
                        "on_the_way" -> {
                            pickUpPassengerMarker()
                        }
                        "arrived" -> {
                            pickUpMarker?.remove()
                            dropOffPassengerMarker()
                        }
                        "start_trip" -> {
                            dropOffPassengerMarker()
                        }
                        "pay" -> {
                            dropOffMarker?.remove()
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        db.child(Constants.TRIPS).child(tripId.toString()).child("status")
            .addValueEventListener(valueEventListenerOnTrip!!)
    }
    private fun observer(){
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.updateAvaResult.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            val data = networkState.data as Resource<UpdateAvailability>
                            val captainStatus = data.getData()?.available
                            preferencesManager.saveBoolean(
                                Constants.CAPTAIN_STATUS,
                                captainStatus!!
                            )
                            updateCaptainStatus()
                        }

                        NetworkState.Status.FAILED -> {
                            updateCaptainStatus()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.onTripResult.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            val data = networkState.data as Resource<PassengerData>
                            tripAccepted = true
                            tripId = data.getData()?.id!!
                            sharedViewModel.setTripId(tripId)
                            Constants.pickUpLatLng = LatLng(
                                data.getData()?.pickup_lat?.toDouble()!!,
                                data.getData()?.pickup_lng?.toDouble()!!
                            )
                            Constants.dropOffLatLng = LatLng(
                                data.getData()?.dropoffLat?.toDouble()!!,
                                data.getData()?.dropoffLng?.toDouble()!!
                            )
                            showBottomSheet(R.navigation.nav_trip)
                            listenerOnTrip()
                        }

                        NetworkState.Status.FAILED -> {
                           // showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                        }

                        else -> {}
                    }
                }
            }
        }
    }


    private fun pickUpPassengerMarker(){
        if(pickUpMarker == null){
            pickUpMarker = addPickUpMarker(Constants.pickUpLatLng!!)
        }
        pickUpMarker?.position = Constants.pickUpLatLng!!
    }
    private fun dropOffPassengerMarker(){
        if (dropOffMarker == null){
            dropOffMarker = addDropOffMarker(Constants.dropOffLatLng!!)
        }
        dropOffMarker?.position = Constants.dropOffLatLng!!
    }
    private fun addPickUpMarker(latLng: LatLng): Marker {
        val bitmapDescriptor =
            BitmapDescriptorFactory.fromBitmap(MapUtils.getPickupBitmap(requireContext()))
        return mMap.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )!!
    }
    private fun addDropOffMarker(latLng: LatLng): Marker {
        val bitmapDescriptor =
            BitmapDescriptorFactory.fromBitmap(MapUtils.getDropOffBitmap(requireContext()))
        return mMap.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )!!
    }
    private fun clearMap(){
        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.checkButtonLayout.visibilityVisible()
        tripAccepted = false
        pickUpMarker = null
        dropOffMarker = null
        mMap.clear()
        Constants.pickUpLatLng = null
        Constants.dropOffLatLng = null
        getLocation()
        Constants.captainLatLng?.let{
            addCarMarkerAndGet(it)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        db.child(Constants.ONLINE_CAPTAINS).child(captainId).child("tripId")
            .removeEventListener(valueEventListener!!)
        if(tripId != 0)
            db.child(Constants.TRIPS).child(tripId.toString()).child("status")
                .removeEventListener(valueEventListenerOnTrip!!)
    }
}