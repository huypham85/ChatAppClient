package com.vn.chat_app_client.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment() {
    companion object {
        const val RECEIVER_ID = "ReceiverId"
    }

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val roomAdapter: RoomAdapter by lazy {
        RoomAdapter(requireContext())
    }

    private val userAdapter: UserAdapter by lazy {
        val listener = object : UserAdapter.UserClickListener {
            override fun onClickUser(userId: String) {
                viewModel.createRoom(userId)
            }
        }
        UserAdapter(listener)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        binding.rcvRoom.layoutManager = LinearLayoutManager(context)
        binding.rcvRoom.adapter = roomAdapter

        binding.rcvUser.layoutManager = LinearLayoutManager(context)
        binding.rcvUser.adapter = userAdapter

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is HomeViewModel.Event.NavigateToChat -> navigateToChat(event.receiverId)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.listUserShow.collect {
                userAdapter.reloadData(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                if (it.modeUser) {
                    binding.rcvUser.visibility = View.VISIBLE
                    binding.rcvRoom.visibility = View.GONE
                } else {
                    binding.rcvUser.visibility = View.GONE
                    binding.rcvRoom.visibility = View.VISIBLE
                }
            }
        }

        return binding.root

    }

    private fun navigateToChat(receiverId: String) {
        val bundle = Bundle()
        bundle.putString(RECEIVER_ID,receiverId)
        findNavController().navigate(R.id.action_homeFragment_to_chatFragment, bundle)
    }


}