package com.mostafahelal.atmodrive2.auth.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.mostafahelal.atmodrive2.BottomSheetFragment
import com.mostafahelal.atmodrive2.R

class CreateAccountVehicalInfoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_create_account_vehical_info, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button=view.findViewById<Button>(R.id.submit_and_continue_vehical)
        val carImage=view.findViewById<ImageView>(R.id.Car_image)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_CreateAccountVehicalInfoFragment_to_CreateAccountBankInfoFragment)

        }
        carImage.setOnClickListener {
           val bottomSheetFragment= BottomSheetFragment()
            bottomSheetFragment.show(childFragmentManager,bottomSheetFragment.tag)

        }
    }
}