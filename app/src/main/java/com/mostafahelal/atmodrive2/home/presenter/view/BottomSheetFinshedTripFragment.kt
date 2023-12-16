package com.mostafahelal.atmodrive2.home.presenter.view

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
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.databinding.FragmentBottomSheetFinshedTripBinding
import com.mostafahelal.atmodrive2.home.domain.model.PassengerData
import com.mostafahelal.atmodrive2.home.domain.model.PassengerDetails
import com.mostafahelal.atmodrive2.home.domain.model.TripStatus
import com.mostafahelal.atmodrive2.home.presenter.viewmodel.SharedViewModel
import com.mostafahelal.atmodrive2.home.presenter.viewmodel.TripViewModel
import com.mostafahelal.atmodrive2.utils.Constants
import com.mostafahelal.atmodrive2.utils.NetworkState
import com.mostafahelal.atmodrive2.utils.Resource
import com.mostafahelal.atmodrive2.utils.getData
import com.mostafahelal.atmodrive2.utils.showToast
import com.mostafahelal.atmodrive2.utils.visibilityGone
import com.mostafahelal.atmodrive2.utils.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetFinshedTripFragment : Fragment() {
    @Inject
    lateinit var preferencesManager: ISharedPreferencesManager
    private lateinit var binding: FragmentBottomSheetFinshedTripBinding
    val sharedViewModel: SharedViewModel by activityViewModels()
    private val tripViewModel : TripViewModel by viewModels()
    private var tripId = 0
    private var tripCost = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentBottomSheetFinshedTripBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentBottomSheetFinshedTripBinding.bind(view)
        tripCost = preferencesManager.getString(Constants.TRIP_COST)

        onClick()
        observer()
        listenerOnTripId()
    }

    private fun listenerOnTripId() {
        sharedViewModel.tripId.observe(requireActivity(), Observer {

            tripId = it
            tripViewModel.getPassengerDetails(it)

        })
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.passengerDetails.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.finshTripProgressBar.visibilityGone()
                            val data = networkState.data as Resource<PassengerDetails>
                            displayPassengerData(data.getData()?.data!!)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.finshTripProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.finshTripProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.confirmCash.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.finshTripProgressBar.visibilityGone()
                            preferencesManager.saveString(Constants.TRIP_COST,"")
                            val data = networkState.data as Resource<TripStatus>
                            sharedViewModel.setRequestStatus(false)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.finshTripProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.finshTripProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun onClick() {
        binding.btnTFConfirm.setOnClickListener {
            val amount = binding.etTFPaidMoney.text.toString()
            if (amount.isNotBlank()){
                tripViewModel.confirmCash(tripId,amount.toDouble())
            }else{
                showToast("enter paid money")
            }
        }
    }

    private fun displayPassengerData(data: PassengerData){
        if (tripCost.isBlank()){
            tripCost = data.cost!!
        }
        binding.apply {
            tvTFPassengerName.text = data.passenger?.fullName
            tvTFTripPrice.text = "$tripCost EGP"
            tvTFFinalPrice.text = "$tripCost EGP"
        }
    }
}