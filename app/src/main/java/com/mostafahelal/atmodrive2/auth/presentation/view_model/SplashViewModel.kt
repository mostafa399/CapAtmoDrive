package com.mostafahelal.atmodrive2.auth.presentation.view_model

import androidx.lifecycle.ViewModel
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.auth.data.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SplashViewModel  @Inject constructor(
    iSharedPreferencesManager: ISharedPreferencesManager
):ViewModel()
{
    val loggedIn:Boolean=iSharedPreferencesManager.userIsLoggedIn()
    val token=iSharedPreferencesManager.getString(Constants.REMEMBER_TOKEN_PREFS)
    val registerStep=iSharedPreferencesManager.getString(Constants.REGISTER_STEP_PREFS)
}