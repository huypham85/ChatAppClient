package com.vn.chat_app_client.presentation.auth.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vn.chat_app_client.data.api.auth.response.RegisterResponse
import com.vn.chat_app_client.databinding.ActivityRegisterBinding
import com.vn.chat_app_client.presentation.auth.AuthActivity
import com.vn.chat_app_client.utils.hideKeyboardOnClickOutside
import com.vn.chat_app_client.utils.setFullScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    companion object {
        const val REGISTER_DATA = "RegisterData"
    }

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.event.collect { event ->
                    when (event) {
                        is RegisterViewModel.Event.NavigateToLogin -> navToLogin(event.registerData)
                        is RegisterViewModel.Event.Error -> showError(event.message)
                    }
                }
            }
        }

        hideKeyboardOnClickOutside(binding.root)
        setFullScreen()
    }

    private fun navToLogin(registerData: RegisterResponse) {
        val intent = Intent(this, AuthActivity::class.java)
        intent.putExtra(REGISTER_DATA,registerData)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
}