package com.vn.chat_app_client.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.api.response.LoadingStatus
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.databinding.ActivityAuthBinding
import com.vn.chat_app_client.ui.MainActivity
import com.vn.chat_app_client.ui.utils.hideKeyboardOnClickOutside
import com.vn.chat_app_client.ui.utils.setFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.loginBtn.setOnClickListener {
            viewModel.checkLogin(
                User(
                    "",
                    binding.phoneEditTxt.text.toString(),
                    binding.passwordEditTxt.text.toString()
                // TODO: use 2 ways binding
                )
            )
            navToHome()
        }

        viewModel.loginResult.observe(this) {
            when (it.loadingStatus) {
                LoadingStatus.Error -> Toast.makeText(
                    this,
                    getString(R.string.auth__login_fail),
                    Toast.LENGTH_LONG,
                ).show()
                LoadingStatus.Success -> navToHome()
                else -> null
            }
        }

        hideKeyboardOnClickOutside(binding.root)
        setFullScreen()
    }

    private fun navToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}