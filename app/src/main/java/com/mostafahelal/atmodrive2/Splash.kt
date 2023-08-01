package com.mostafahelal.atmodrive2

import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler


class Splash : Fragment(R.layout.fragment_splash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startSplashScreen()


}
    fun startSplashScreen(){
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)
            findNavController().navigate(R.id.action_splash_to_intro)
        }
    }
}