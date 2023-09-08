package com.mostafahelal.atmodrive2.auth.presentation.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mostafahelal.atmodrive2.R
import com.mostafahelal.atmodrive2.auth.presentation.view_model.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    @Inject
    lateinit var viewModel:SplashViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                if (viewModel.loggedIn){
                    withContext(Dispatchers.Main){
                    val action=SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                    findNavController().navigate(action)}
                }else{
                    withContext(Dispatchers.Main){
                    Handler(Looper.getMainLooper()).postDelayed({
                        val action= SplashFragmentDirections.actionSplashFragmentToIntroFragment()
                        findNavController().navigate(action)
                    },3000L)
                }
                }
            }
        }

}

}