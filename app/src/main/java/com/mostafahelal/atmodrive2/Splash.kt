package com.mostafahelal.atmodrive2

import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import java.util.logging.Handler


class Splash : Fragment(R.layout.fragment_splash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        android.os.Handler(Looper.getMainLooper()).postDelayed({
             findNavController().navigate(R.id.action_splash_to_intro)

        },3000)


}
}