package com.mostafahelal.atmodrive2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class Intro : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button=view.findViewById<Button>(R.id.login_btn)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_intro_to_forgetPassword)
        }
        val createAccount_btn=view.findViewById<Button>(R.id.create_acc_btn)
        createAccount_btn.setOnClickListener {
            findNavController().navigate(R.id.action_intro_to_createAccount)
        }
    }
}