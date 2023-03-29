package com.vn.chat_app_client.presentation.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.model.SampleModel
import com.vn.chat_app_client.databinding.FragmentSettingsBinding
import com.vn.chat_app_client.presentation.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding

    private var imgUri: Uri? = null
    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            result?.let {
                if (result.resultCode == RESULT_OK) {
                    val intent = result.data
                    intent?.data?.let {
                        imgUri = it
                    }
                    binding.imgAvt.setImageURI(imgUri)
                    viewModel.getURLAfterUpload(imgUri)
                    viewModel.uriLiveData.observe(viewLifecycleOwner) {
                        Log.d("AAA", it)
                    }
                }
            }
        }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgAvt.setOnClickListener {
            val gallery = Intent()
            gallery.action = ACTION_GET_CONTENT
            gallery.type = "image/*"
            activityResultLauncher.launch(gallery)
        }

//        binding.testBtn.setOnClickListener {
//
//            pushAndGetDataFromRoom()
//        }
    }

    private fun navToLogin() {
        activity?.let {
            val intent = Intent(it, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
            it.finishAfterTransition()
        }
    }

    private fun navToNewGroup() {
        findNavController().navigate(R.id.action_settingsFragment_to_newGroupFragment)
    }

//    private fun pushAndGetDataFromRoom(){
//        binding.testTxt.text = ""
//        val sampleModel = SampleModel(name = binding.fullNameTxt.text.toString())
//        viewModel.insertMessage(sampleModel)
//        viewModel.messageLiveData.observe(viewLifecycleOwner){
//            binding.testTxt.text = ""
//            it.forEach { it1 ->
//                binding.testTxt.append(it1.name +"\n")
//            }
//            Log.e("longtq", "pushDataToRoom: "+it.size.toString() )
//        }
//    }
}