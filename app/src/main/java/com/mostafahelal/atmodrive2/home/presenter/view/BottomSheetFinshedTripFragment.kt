package com.mostafahelal.atmodrive2.home.presenter.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mostafahelal.atmodrive2.R
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.databinding.FragmentBottomSheetFinshedTripBinding
import com.mostafahelal.atmodrive2.databinding.FragmentBotttomSheetAcceptedTripBinding
import com.mostafahelal.atmodrive2.home.presenter.viewmodel.SharedViewModel
import com.mostafahelal.atmodrive2.home.presenter.viewmodel.TripViewModel
import com.mostafahelal.atmodrive2.utils.Constants
import com.mostafahelal.atmodrive2.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetFinshedTripFragment : Fragment() {
    @Inject
    lateinit var preferencesManager: ISharedPreferencesManager
    private lateinit var binding: FragmentBottomSheetFinshedTripBinding
    val model: SharedViewModel by activityViewModels()
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
    }

    private fun observer() {
    }

    private fun onClick() {

    }


}