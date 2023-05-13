package com.vn.chat_app_client.presentation.auth

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vn.chat_app_client.data.model.SampleModel
import com.vn.chat_app_client.databinding.ActivityAuthBinding
import com.vn.chat_app_client.presentation.MainActivity
import com.vn.chat_app_client.presentation.auth.register.RegisterActivity
import com.vn.chat_app_client.utils.hideKeyboardOnClickOutside
import com.vn.chat_app_client.utils.setFullScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        viewModel.checkHasLoggedIn()

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
        val items = viewModel.uiState.value.users
        val mockItems = mutableListOf(SampleModel(0,"huy","huy"),SampleModel(0,"tu","huy"))
        adapter = ArrayAdapter(this@AuthActivity,R.layout.simple_dropdown_item_1line,viewModel.userList.map { it.username })
        binding.phoneEditTxt.setAdapter(adapter)




        binding.phoneEditTxt.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedItem = items[position]
                selectedItem.let {
                    binding.phoneEditTxt.setText(it.username)
                    binding.passwordEditTxt.setText(it.password)

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