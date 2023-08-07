package com.mostafahelal.atmodrive2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController

class CreateAccount : Fragment() {
 lateinit var button: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         button=view.findViewById<Button>(R.id.next1_btn)
        button.setOnClickListener {
            val action=CreateAccountDirections.actionCreateAccountToStep2CreateAccount()
            findNavController().navigate(action)
        }
    }
}