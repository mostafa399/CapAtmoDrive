package com.mostafahelal.atmodrive2.home.presenter.view
import android.os.Bundle
import android.os.CountDownTimer
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
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mostafahelal.atmodrive2.R
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.databinding.FragmentBottomSheetNewTripBinding
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
import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject
@AndroidEntryPoint
class BottomSheetNewTripFragment : Fragment() {
    @Inject
    lateinit var preferencesManager: ISharedPreferencesManager
    private lateinit var binding: FragmentBottomSheetNewTripBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val tripViewModel : TripViewModel by viewModels()
    private var timer:Long = 60000
    private var timerCounter: CountDownTimer? = null
    private lateinit var db: DatabaseReference
    private var capId = ""
    private var tripId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBottomSheetNewTripBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentBottomSheetNewTripBinding.bind(view)
        init()
        startCountdownTimer()
        onClick()
        listenerOnTripId()
        observer()
    }
    private fun init() {
        db = Firebase.database.reference
        capId = preferencesManager.getString(Constants.CAPTAIN_ID)
    }

    private fun onClick() {
        binding.btnAcceptTrip.setOnClickListener {
            tripViewModel.acceptTrip(
                tripId,
                Constants.captainLatLng?.latitude.toString(),
                Constants.captainLatLng?.longitude.toString(),
               sharedViewModel.getAddressFromLatLng(requireContext(),Constants.captainLatLng!!)
            )
        }

        binding.btnRejectTrip.setOnClickListener {
            db.child(Constants.ONLINE_CAPTAINS).child(capId).child("tripId").setValue(0)
            //it.disable()
            sharedViewModel.setTripStatus(false)
        }
    }

    private fun observer(){
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.passengerDetails.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.newRequestProgressBar.visibilityGone()
                            val data = networkState.data as Resource<PassengerDetails>
                            showPassengerData(data.getData()?.data)
                            timerCounter?.start()
                        }

                        NetworkState.Status.FAILED -> {
                            binding.newRequestProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.newRequestProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.acceptTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.newRequestProgressBar.visibilityGone()
                            binding.btnAcceptTrip.enabled()
                            val data = networkState.data as Resource<TripStatus>
                            sharedViewModel.setRequestStatus(true)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.newRequestProgressBar.visibilityGone()
                            binding.btnAcceptTrip.enabled()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.newRequestProgressBar.visibilityVisible()
                            binding.btnAcceptTrip.disable()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun listenerOnTripId() {
        sharedViewModel.tripId.observe(requireActivity(), Observer {

            tripViewModel.getPassengerDetails(it)

        })
    }

    private fun showPassengerData(data: PassengerData?){
        tripId = data?.id!!
        binding.apply {
            tvLocFromWhere.text = data.dropoffLocationName
            tvLocToWhere.text = data.pickup_location_name
            tvPassengerName.text = data.passenger?.fullName
            tvTripPrice.text = "${data.cost} EGP"
        }
        Constants.pickUpLatLng = LatLng(data.pickup_lat!!.toDouble(),data.pickup_lng!!.toDouble())
        Constants.dropOffLatLng = LatLng(data.dropoffLat!!.toDouble(),data.dropoffLng!!.toDouble())
    }

    private fun startCountdownTimer(){
        timerCounter = object : CountDownTimer(timer, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimerRequest.apply {
                    val f: NumberFormat = DecimalFormat("00")
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    val mText = (f.format(min)).toString() + ":" + f.format(sec)
                    text = mText
                    timer = millisUntilFinished
                    binding.progressBarRequestTrip.progressDrawable = resources.getDrawable(R.drawable.request_trip_green_progress_bar)
                    if (sec <= 10){
                        binding.progressBarRequestTrip.progressDrawable = resources.getDrawable(R.drawable.request_trip_red_progress_bar)
                    }
                    binding.progressBarRequestTrip.progress = 100-(sec.toInt() * 100 / 60)
                }
            }

            override fun onFinish() {
                binding.tvTimerRequest.apply {
                    text = "00:00"
                    db.child(Constants.ONLINE_CAPTAINS).child(capId).child("tripId").setValue(0)
                }
                binding.btnRejectTrip.disable()
                binding.btnAcceptTrip.disable()
                cancel()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerCounter?.cancel()
        timerCounter = null

    }

}