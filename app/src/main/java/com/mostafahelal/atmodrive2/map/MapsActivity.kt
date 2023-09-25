package com.mostafahelal.atmodrive2.map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mostafahelal.atmodrive2.MainActivity
import com.mostafahelal.atmodrive2.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
    }

    fun logOut(view: View) {
        startActivity(Intent(applicationContext,MainActivity::class.java))
        finish()
    }
}