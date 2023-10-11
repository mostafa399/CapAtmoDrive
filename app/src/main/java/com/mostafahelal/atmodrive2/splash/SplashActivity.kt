package com.mostafahelal.atmodrive2.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mostafahelal.atmodrive2.auth.presentation.view.MainActivity
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.utils.Constants
import com.mostafahelal.atmodrive2.auth.presentation.view_model.SplashViewModel
import com.mostafahelal.atmodrive2.databinding.ActivitySplashBinding
import com.mostafahelal.atmodrive2.home.MapsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private var _binding:ActivitySplashBinding?=null
    private val binding get() = _binding
    @Inject
    lateinit var viewModel: SplashViewModel

    @Inject
    lateinit var shared:ISharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val token=shared.getString(Constants.REMEMBER_TOKEN_PREFS)
        val registerStep=shared.getString(Constants.REGISTER_STEP_PREFS)
        lifecycleScope.launch {
              if (token.isNullOrBlank()||(registerStep!="3" &&registerStep!="2"))
                {
                    delay(3000L)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
                else
                {
                    startActivity(Intent(applicationContext, MapsActivity::class.java))
                    finish()
                }
        }


    }

}