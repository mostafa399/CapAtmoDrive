package com.mostafahelal.atmodrive2.home.presenter.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.databinding.FragmentBotttomSheetAcceptedTripBinding
import com.mostafahelal.atmodrive2.home.domain.model.PassengerData
import com.mostafahelal.atmodrive2.home.domain.model.PassengerDetails
import com.mostafahelal.atmodrive2.home.domain.model.TripStatus
import com.mostafahelal.atmodrive2.home.presenter.viewmodel.SharedViewModel
import com.mostafahelal.atmodrive2.home.presenter.viewmodel.TripViewModel
import com.mostafahelal.atmodrive2.utils.Constants
import com.mostafahelal.atmodrive2.utils.NetworkState
import com.mostafahelal.atmodrive2.utils.Resource
import com.mostafahelal.atmodrive2.utils.disable
import com.mostafahelal.atmodrive2.utils.enabled
import com.mostafahelal.atmodrive2.utils.getData
import com.mostafahelal.atmodrive2.utils.showToast
import com.mostafahelal.atmodrive2.utils.visibilityGone
import com.mostafahelal.atmodrive2.utils.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint

class BotttomSheetAcceptedTripFragment : Fragment() {
    @Inject
    lateinit var preferencesManager: ISharedPreferencesManager
    private lateinit var binding: FragmentBotttomSheetAcceptedTripBinding
    val model: SharedViewModel by activityViewModels()
    private val tripViewModel : TripViewModel by viewModels()

    private var valueEventListener : ValueEventListener?= null
    private lateinit var database: DatabaseReference
    var tripId = 0
    var tripStats:String? = ""
    var dropOffLatLng: LatLng? = null
    var dropOffLocName:String = ""
    var passengerMobile = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBotttomSheetAcceptedTripBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentBotttomSheetAcceptedTripBinding.bind(view)
        database = Firebase.database.reference
        onClick()
        listenerOnTripId()
        observer()

    }

    private fun onClick() {
        binding.btnTripLifecycle.setOnClickListener {
            when (tripStats) {
                "accepted" -> {
                    tripViewModel.pickUpTrip(tripId)
                }
                "on_the_way" -> {
                    tripViewModel.arrivedTrip(tripId)
                }
                "arrived" -> {
                    tripViewModel.startTrip(tripId)
                }
                "start_trip" -> {
                    tripViewModel.endTrip(
                        tripId,
                        Constants.captainLatLng?.latitude.toString(),
                        Constants.captainLatLng?.longitude.toString(),
                        model.getAddressFromLatLng(requireContext(),Constants.captainLatLng!!),
                        1500.0
                    )
                }
                "pay" -> {

                }
            }
        }
        binding.imgCallPassenger.setOnClickListener {
            val phoneNumber = Uri.parse("tel:$passengerMobile")
            val callIntent = Intent(Intent.ACTION_DIAL, phoneNumber)
            startActivity(callIntent)
        }
    }




    private fun listenerOnTripId() {
        model.tripId.observe(requireActivity(), Observer {

            if (it > 0){
                tripId = it
                listenerOnTrip()
            }

        })
    }

    private fun listenerOnTrip(){
        valueEventListener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val stats = snapshot.getValue(String::class.java)

                tripStats = stats

                when (tripStats) {
                    null -> {
                        model.setRequestStatus(false)
                    }
                    "accepted" -> {
                        tripViewModel.getPassengerDetails(tripId)
                        binding.tvTripLifecycle.text = "trip accepted"
                        binding.btnTripLifecycle.text = "On The Way"
                        binding.imgCallPassenger.enabled()
                    }
                    "on_the_way" -> {
                        tripViewModel.getPassengerDetails(tripId)
                        binding.tvTripLifecycle.text = "Going to pickup"
                        binding.btnTripLifecycle.text = "Arrived"
                        binding.imgCallPassenger.enabled()
                    }
                    "arrived" -> {
                        tripViewModel.getPassengerDetails(tripId)
                        binding.tvTripLifecycle.text = "Waiting for passenger"
                        binding.btnTripLifecycle.text = "Start trip"
                        binding.imgCallPassenger.enabled()
                    }
                    "start_trip" -> {
                        tripViewModel.getPassengerDetails(tripId)
                        binding.tvTripLifecycle.text = "Trip running"
                        binding.btnTripLifecycle.text = "Finish trip"
                        binding.imgCallPassenger.disable()
                    }
                    "pay" -> {
                        val action = BotttomSheetAcceptedTripFragmentDirections.actionBotttomSheetAcceptedTripFragmentToBottomSheetFinshedTripFragment()
                        findNavController().navigate(action)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        database.child("trips").child(tripId.toString()).child("status")
            .addValueEventListener(valueEventListener!!)
    }
    private fun observer(){
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.passengerDetails.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as Resource<PassengerDetails>
                            displayPassengerData(data.getData()?.data!!)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.pickUpTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as Resource<TripStatus>
//                        showToast(data.getData()?.message!!)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.arrivedTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as Resource<TripStatus>
//                        showToast(data.getData()?.message!!)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.startTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as Resource<TripStatus>
//                        showToast(data.getData()?.message!!)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.endTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as Resource<TripStatus>
//                        showToast(data.getData()?.message!!)
//

                        //        preferencesManager.saveString(
//                                Constants.TRIP_COST,
//                                data.getData()?.
//                            )



//                        val action = TripLifecycleFragmentDirections.actionTripLifecycleFragmentToTripFinishedFragment()
//                        mNavController.navigate(action)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.cancelTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as Resource<TripStatus>
                            showToast(data.getData()?.message!!)
                            model.setRequestStatus(false)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
    }
    private fun displayPassengerData(data: PassengerData){
        dropOffLatLng = LatLng(data.dropoffLng!!.toDouble(),data.dropoffLng.toDouble())
        dropOffLocName = data.dropoffLocationName!!
        passengerMobile = data.passenger?.mobile!!
        binding.apply {
            tvTripPassengerName.text = data.passenger.fullName
            tvTripLifecyclePrice.text = "${data.cost} EGP"
            tvEndLoc.text = data.dropoffLocationName
            tvStartingLoc.text = data.pickup_location_name
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        database.child("trips").child(tripId.toString()).child("status")
            .removeEventListener(valueEventListener!!)
    }


}