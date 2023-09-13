package com.mostafahelal.atmodrive2.auth.presentation.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.mostafahelal.atmodrive2.auth.data.models.SendCodeResponse
import com.mostafahelal.atmodrive2.auth.data.utils.NetworkState
import com.mostafahelal.atmodrive2.auth.data.utils.Resource
import com.mostafahelal.atmodrive2.auth.presentation.view_model.AuthViewModel
import com.mostafahelal.atmodrive2.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Response

@AndroidEntryPoint
class IntroFragment : Fragment() {
    private lateinit var introBinding:FragmentIntroBinding
    private val viewModel:AuthViewModel by viewModels()
    private var backPressedCounter = 0
    private val doubleBackPressInterval = 2000


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        introBinding=FragmentIntroBinding.inflate(layoutInflater)
        introBinding.ccp.setCountryForPhoneCode(20)
        return introBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                introBinding=FragmentIntroBinding.bind(view)
                introBinding.loginBtn.isEnabled = false
                setupBackPressedHandler()
                introBinding.otpCode.addTextChangedListener(object :TextWatcher{
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            if (s?.length == 4) {
                                // Hide the keyboard
                                hideKeyboard()
                            }
                        }

                        override fun afterTextChanged(s: Editable?) {
                        }

                    })
                introBinding.phoneEt.addTextChangedListener(object :TextWatcher{
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            if (s?.length == 10) {
                                // Hide the keyboard
                                hideKeyboard()
                            }
                        }

                        override fun afterTextChanged(s: Editable?) {
                        }

                    })
                setupSendOtpCode()
                setupVerifyButton()

    }

    private fun setupSendOtpCode() {
        introBinding.sendCode.setOnClickListener {
            val phoneNumber = introBinding.phoneEt.editableText.toString()
            if (phoneNumber.length == 10) {
                val validPrefixes = listOf("11","12", "15", "10")
                if (validPrefixes.any { phoneNumber.startsWith(it) }) {
                    val phone = "0$phoneNumber"
                    viewModel.sendMobilePhone(phone)
                    observeSendCodeResult()
                    introBinding.loginBtn.isEnabled = false
                } else {
                    Toast.makeText(requireContext(), "Phone number does not exist", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun setupVerifyButton() {

        introBinding.loginBtn.setOnClickListener {
            val otpCode = introBinding.otpCode.editableText.toString()
            val phoneNumber = introBinding.phoneEt.editableText.toString()
            val deviceToken = "device_token:0$phoneNumber"
            viewModel.checkCode("0$phoneNumber",otpCode,deviceToken)
            observeNavigateToRegister()
            observeNavigateToMain()

        }
    }
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(introBinding.otpCode.windowToken, 0)
    }
    private fun observeSendCodeResult(){
        lifecycleScope.launch (Dispatchers.IO){
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.sendCodeResult.collect{networkState->
                    when(networkState?.status){
                        NetworkState.Status.SUCCESS->{


                               withContext(Dispatchers.Main){
                                   Toast.makeText(requireContext(), "Phone number posted to the server", Toast.LENGTH_SHORT).show()
                                   introBinding.loginBtn.isEnabled = true

                               }

                        }
                        NetworkState.Status.FAILED->{
                            Log.d("IntroFragment", networkState.msg.toString())
                        //    introBinding.loginBtn.isEnabled = true

                        }
                        else -> {
                       //     introBinding.loginBtn.isEnabled = true

                        }
                    }

                }
            }
        }
    }
    private fun observeNavigateToRegister() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToRegister.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            val phone=introBinding.phoneEt.editableText.toString()
                            withContext(Dispatchers.Main){
//                                val action=IntroFragmentDirections.actionIntroFragmentToCreateAccountVehicalInfoFragment()
                                val action=IntroFragmentDirections.actionIntroFragmentToCreateAccountPersonalInfoFragment("0$phone")
                                findNavController().navigate(action)
                            }}
                        NetworkState.Status.FAILED->{
                            Log.d("VerifyFragment ", networkState.msg.toString())

                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun observeNavigateToMain() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToMain.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            withContext(Dispatchers.Main){
                                val action=IntroFragmentDirections.actionIntroFragmentToHomeFragment()
                                findNavController().navigate(action)
                            }}
                        NetworkState.Status.FAILED->{
                            Log.d("VerifyFragment", networkState.msg.toString())

                        }
                        else -> Unit
                    }
                }
            }
        }
    }
    private fun setupBackPressedHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedCounter == 1) {
                    // User pressed back button twice within the interval, exit the app
                    requireActivity().finish()
                } else {
                    // User pressed back button once, show a toast
                    Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show()
                    backPressedCounter++

                    // Reset the counter after the interval
                    Handler(Looper.getMainLooper()).postDelayed({
                        backPressedCounter = 0
                    }, doubleBackPressInterval.toLong())
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    override fun onResume() {
        super.onResume()
        // Reset the backPressedCounter when the fragment resumes
        backPressedCounter = 0
    }
}