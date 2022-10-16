package com.vn.chat_app_client.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vn.chat_app_client.databinding.ActivitySplashBinding
import com.vn.chat_app_client.presentation.auth.AuthActivity
import com.vn.chat_app_client.presentation.MainActivity
import com.vn.chat_app_client.utils.setFullScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreen()

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.events.collect { event ->
                    when (event) {
                        is SplashViewModel.Event.NavigateToLogin -> navigateTo(AuthActivity::class.java)
                        is SplashViewModel.Event.NavigateToHome -> navigateTo(MainActivity::class.java)
                    }
                }
            }
        }


        viewModel.checkLogin()
    }

    private fun <T> navigateTo(cls: Class<T>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, cls)
            startActivity(intent)
            finish()
        }, 1800)
    }
}