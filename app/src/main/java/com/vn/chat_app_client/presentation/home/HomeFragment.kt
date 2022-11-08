package com.vn.chat_app_client.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vn.chat_app_client.R
import com.vn.chat_app_client.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    companion object {
        const val ROOM_ID = "RoomId"
    }

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val roomAdapter: RoomAdapter by lazy {
        val listener = object : RoomAdapter.RoomClickListener {
            override fun onClickRoom(idRoom: String) {
                viewModel.navToChat(idRoom)
            }
        }
        RoomAdapter(listener)
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
                    is HomeViewModel.Event.NavigateToChat -> navigateToChat(event.roomId)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.listUserShow.collect {
                userAdapter.reloadData(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.listRoomShow.collect {
                roomAdapter.reloadData(it)
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

    private fun navigateToChat(roomId: String) {
        val bundle = Bundle()
        bundle.putString(ROOM_ID, roomId)
        findNavController().navigate(R.id.action_homeFragment_to_chatFragment, bundle)
    }


}