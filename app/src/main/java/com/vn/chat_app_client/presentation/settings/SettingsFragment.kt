package com.vn.chat_app_client.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vn.chat_app_client.R
import com.vn.chat_app_client.databinding.FragmentSettingsBinding
import com.vn.chat_app_client.presentation.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                binding.fullNameTxt.text = it.fullName
                binding.userNameTxt.text = it.username
            }
        }

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.event.collect { event ->
                    when (event) {
                        is SettingsViewModel.Event.NavigateToNewGroup -> navToNewGroup()
                        is SettingsViewModel.Event.NavigateToLogin -> navToLogin()
                    }
                }
            }
        }
        return binding.root
    }

    private fun navToLogin() {
        activity?.let{
            val intent = Intent (it, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
            it.finishAfterTransition()
        }
    }

    private fun navToNewGroup() {
        findNavController().navigate(R.id.action_settingsFragment_to_newGroupFragment)
    }



}