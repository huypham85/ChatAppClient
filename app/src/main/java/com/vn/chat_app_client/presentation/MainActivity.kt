package com.vn.chat_app_client.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.vn.chat_app_client.R
import com.vn.chat_app_client.databinding.ActivityMainBinding
import com.vn.chat_app_client.utils.setKeyboardVisibilityListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragment_activity_main) as NavHostFragment
    }
    private val navController: NavController by lazy {
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
            setKeyboardVisibilityListener { visibility ->
                binding.bottomNavigationView.isVisible = !visibility
            }
        }

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.chatFragment) {
                binding.bottomNavigationView.visibility = View.GONE
            }
        }
    }
}