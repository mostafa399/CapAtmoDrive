package com.mostafahelal.atmodrive2.map
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mostafahelal.atmodrive2.MainActivity
import com.mostafahelal.atmodrive2.R
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.auth.data.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {
    @Inject
    lateinit var preferencesManager: ISharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
    }
    fun logOut(view: View) {
        preferencesManager.clearString(Constants.REMEMBER_TOKEN_PREFS)
        preferencesManager.clearString(Constants.REGISTER_STEP_PREFS)
        startActivity(Intent(applicationContext,MainActivity::class.java))
        finish()
    }
}