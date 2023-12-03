package com.mostafahelal.atmodrive2.auth.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.utils.Constants
import com.mostafahelal.atmodrive2.utils.NetworkState
import com.mostafahelal.atmodrive2.utils.Resource
import com.mostafahelal.atmodrive2.utils.showToast
import com.mostafahelal.atmodrive2.utils.visibilityGone
import com.mostafahelal.atmodrive2.utils.visibilityVisible
import com.mostafahelal.atmodrive2.auth.domain.model.RegisterResponseModel
import com.mostafahelal.atmodrive2.auth.presentation.view_model.AuthViewModel
import com.mostafahelal.atmodrive2.databinding.FragmentCreateAccountBankInfoBinding
import com.mostafahelal.atmodrive2.home.presenter.view.MapsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateAccountBankInfoFragment : Fragment() {
    private lateinit var binding:FragmentCreateAccountBankInfoBinding
    @Inject
    lateinit var shared: ISharedPreferencesManager
    private val viewModel:AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCreateAccountBankInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentCreateAccountBankInfoBinding.bind(view)
        setupToolbarNavigation()
        setupBackPressedHandler()
        binding.skip.setOnClickListener {
            startActivity(Intent(requireContext(), MapsActivity::class.java))
           activity?.finish()

        }
        binding.submitAndContinueBank.setOnClickListener {
            if (binding.BankName.text.toString().isNotBlank()&&binding.IBANNumber.text.toString().isNotBlank()&&
                binding.AccountPersonalName.text.toString().isNotBlank()&& binding.AccountNumber.text.toString().isNotBlank()){

                viewModel.registerBankAccount(binding.BankName.text.toString(),binding.IBANNumber.text.toString(),
                    binding.AccountPersonalName.text.toString(),binding.AccountNumber.text.toString())

            }
        }

        observeOnRegisterBankAccount()

    }
    fun observeOnRegisterBankAccount(){
        lifecycleScope.launch {
            viewModel.bankAccount.collect{
                when(it?.status){
                    NetworkState.Status.SUCCESS->{
                        startActivity(Intent(requireContext(), MapsActivity::class.java))
                        activity?.finish()
                        val data=it.data as Resource<RegisterResponseModel>
                        shared.saveString(Constants.REGISTER_STEP_PREFS,data.data?.data?.register_step.toString())
                        binding.pBankAccount.visibilityGone()

                    }
                    NetworkState.Status.FAILED->{
                        binding.pBankAccount.visibilityGone()
                        showToast(it.msg.toString())


                    }
                    NetworkState.Status.RUNNING->{
                        binding.pBankAccount.visibilityVisible()
                    }
                    else -> {}
                }
            }
        }
    }
    private fun setupBackPressedHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    private fun setupToolbarNavigation() {
        binding.topappbar.setNavigationOnClickListener {
            requireActivity().finish()


        }
    }

}