package com.mostafahelal.atmodrive2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.mostafahelal.atmodrive2.databinding.ItemBottomSheetBinding

class Step4CreateAccount : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_step4_create_account, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button=view.findViewById<Button>(R.id.next2_btn)
        val carImage=view.findViewById<ImageView>(R.id.Car_images)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_step4CreateAccount_to_step5CreateAccount)

        }
        carImage.setOnClickListener {
           val bottomSheetFragmen= BottomSheetFragment()
            bottomSheetFragmen.show(childFragmentManager,bottomSheetFragmen.tag)

        }
    }
}