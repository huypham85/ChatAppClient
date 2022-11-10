package com.vn.chat_app_client.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vn.chat_app_client.databinding.ActivityAuthBinding
import com.vn.chat_app_client.presentation.MainActivity
import com.vn.chat_app_client.presentation.auth.register.RegisterActivity
import com.vn.chat_app_client.utils.hideKeyboardOnClickOutside
import com.vn.chat_app_client.utils.setFullScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel

        viewModel.getDataFromRegister(intent)

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.event.collect { event ->
                    when (event) {
                        is AuthViewModel.Event.NavigateToHome -> navToHome()
                        is AuthViewModel.Event.NavigateToRegister -> navToRegister()
                    }
                }
            }
        }

        hideKeyboardOnClickOutside(binding.root)
        setFullScreen()
    }

    private fun navToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAfterTransition()
    }

    private fun navToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finishAfterTransition()
    }
}