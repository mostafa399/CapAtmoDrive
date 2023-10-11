package com.mostafahelal.atmodrive2.auth.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mostafahelal.atmodrive2.utils.NetworkState
import com.mostafahelal.atmodrive2.utils.Resource
import com.mostafahelal.atmodrive2.utils.disable
import com.mostafahelal.atmodrive2.utils.enabled
import com.mostafahelal.atmodrive2.utils.getData
import com.mostafahelal.atmodrive2.utils.visibilityGone
import com.mostafahelal.atmodrive2.utils.visibilityVisible
import com.mostafahelal.atmodrive2.auth.domain.model.LoginResponseModel
import com.mostafahelal.atmodrive2.auth.presentation.view_model.AuthViewModel
import com.mostafahelal.atmodrive2.databinding.FragmentIntroBinding
import com.mostafahelal.atmodrive2.home.MapsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat

@AndroidEntryPoint
class IntroFragment : Fragment() {
    private lateinit var introBinding:FragmentIntroBinding
    private val viewModel:AuthViewModel by viewModels()
    private var backPressedCounter = 0
    private val doubleBackPressInterval = 2000
    private var mTimer:Long = 120000
    private var countdownTimer: CountDownTimer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        introBinding=FragmentIntroBinding.inflate(layoutInflater)
        introBinding.ccp.setCountryForPhoneCode(20)
        return introBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        introBinding=FragmentIntroBinding.bind(view)

        if (savedInstanceState != null) {
            mTimer = savedInstanceState.getLong("time",120000)
            if(mTimer != 120000.toLong()){
                startCountdownTimer()
                countdownTimer?.start()
            }
        }
        introBinding.resendCode.setOnClickListener {
            val mobile = introBinding.phoneEt.text.toString()
            viewModel.sendMobilePhone("0$mobile")
            mTimer = 120000
            startCountdownTimer()
        }
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
            if (otpCode.length==4){
            viewModel.checkCode("0$phoneNumber",otpCode,deviceToken)
            observeNavigateToRegister()
            }
            else{
                Toast.makeText(requireContext(), "Please write Code Correctly", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun observeSendCodeResult() {
       viewLifecycleOwner.lifecycleScope.launch {
           repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.sendCodeResult.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            introBinding.loginBtn.isEnabled = true
                            introBinding.loginprogressbar.visibilityGone()
                            countdownTimer?.start()
                        }

                        NetworkState.Status.FAILED -> {
                            introBinding.resendCode.apply {
                                text = "Resend"
                                disable()
                            }
                            countdownTimer?.cancel()
                            introBinding.loginprogressbar.visibilityGone()
                        }

                        NetworkState.Status.RUNNING -> {
                            introBinding.loginprogressbar.visibilityVisible()
                        }

                        else -> {
                        }
                    }


                }
            }
       }
        }

    private fun observeNavigateToRegister() {
       viewLifecycleOwner.lifecycleScope.launch {
           repeatOnLifecycle(Lifecycle.State.STARTED){
           viewModel.navigateToRegister.collect { networkState ->
                    try {
                        when (networkState?.status) {
                            NetworkState.Status.SUCCESS -> {
                                countdownTimer?.cancel()
                                val data = networkState.data as Resource<LoginResponseModel>
                                val phone = introBinding.phoneEt.editableText.toString()
                                if (data.getData()?.data?.is_new == true) {

//                                    val action = IntroFragmentDirections.actionIntroFragmentToCreateAccountVehicalInfoFragment()
                                    val action = IntroFragmentDirections.actionIntroFragmentToCreateAccountPersonalInfoFragment("0$phone")
                                    findNavController().navigate(action)
                                } else {
                                    if (data.getData()?.data?.user?.register_step==1){
                                        val action=IntroFragmentDirections.actionIntroFragmentToCreateAccountVehicalInfoFragment()
                                        findNavController().navigate(action)
                                    }
                                    else{
                                        val intent = Intent(requireContext(), MapsActivity::class.java)
                                        startActivity(intent)
                                        activity?.finish()
                                    }

                                }
                                introBinding.loginprogressbar.visibilityGone()

                            }

                            NetworkState.Status.FAILED -> {
                             //   showToastShort("Error code ")
                                introBinding.loginprogressbar.visibilityGone()

                            }

                            NetworkState.Status.RUNNING -> {
                                introBinding.loginprogressbar.visibilityVisible()

                            }

                            else -> {}
                        }
                    } catch (e: Exception) {

                        e.localizedMessage
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
    private fun startCountdownTimer(){
        countdownTimer = object : CountDownTimer(mTimer, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                introBinding.resendCode.apply {
                    val f: NumberFormat = DecimalFormat("00")
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    val mText =
                        "<font color='#B2C3C9'>Resend(${(f.format(min)).toString() + ":" + f.format(sec)}s)</font>"
                    setText(Html.fromHtml(mText), TextView.BufferType.SPANNABLE)
                    disable()
                    mTimer = millisUntilFinished
                }
            }

            override fun onFinish() {
                introBinding.resendCode.apply {
                    val mText = "<font color='#00A6A6'><u>Resend</u></font>"
                    setText(Html.fromHtml(mText), TextView.BufferType.SPANNABLE)
                    enabled()
                }
                cancel()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(introBinding.otpCode.windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
        backPressedCounter = 0
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong("time", mTimer)

    }

}