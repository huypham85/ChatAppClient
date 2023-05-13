package com.vn.chat_app_client.presentation.group

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.databinding.FragmentGroupBinding
import com.vn.chat_app_client.presentation.home.RoomAdapter
import com.vn.chat_app_client.presentation.home.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GroupFragment : Fragment() {
    companion object {
        const val ROOM_ID = "RoomId"
    }

    @Inject
    lateinit var savedAccountManager: SavedAccountManager

    private lateinit var binding: FragmentGroupBinding
    private val viewModel: GroupViewModel by viewModels()

    private val roomAdapter: RoomAdapter by lazy {
        val listener = object : RoomAdapter.RoomClickListener {
            override fun onClickRoom(idRoom: String) {
                viewModel.navToChat(idRoom)
            }
        }
        RoomAdapter(listener, savedAccountManager)
    }

    private val userAdapter: UserAdapter by lazy {
        val listener = object : UserAdapter.UserClickListener {
            override fun onClickUser(userId: String) {
                viewModel.createRoom(userId)
            }
        }
        UserAdapter(listener,requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        binding.rcvRoom.layoutManager = LinearLayoutManager(context)
        binding.rcvRoom.adapter = roomAdapter

        binding.rcvUser.layoutManager = LinearLayoutManager(context)
        binding.rcvUser.adapter = userAdapter

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is GroupViewModel.Event.NavigateToChat -> navigateToChat(event.roomId)
                    is GroupViewModel.Event.NavigateToNewGroup -> navToNewGroup()
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
            viewModel.messageReceivedFlow.collect {
                viewModel.getData()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                if (it.modeUser) {
                    binding.swipeToRefreshUser.visibility = View.VISIBLE
                    binding.swipeToRefreshRoom.visibility = View.GONE
                } else {
                    binding.swipeToRefreshUser.visibility = View.GONE
                    binding.swipeToRefreshRoom.visibility = View.VISIBLE
                }
                it.imgAvt?.let { avt ->
                    Glide.with(requireContext()).load(avt).into(binding.imgAvt)
                }
            }
        }

        binding.swipeToRefreshRoom.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            viewModel.getData()
            binding.swipeToRefreshRoom.isRefreshing = false
        })

        binding.swipeToRefreshUser.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            binding.swipeToRefreshUser.isRefreshing = false
        })

        binding.btnGroupAdd.setOnClickListener {
            viewModel.navigateToNewGroup()
        }

        return binding.root
    }

    private fun navToNewGroup() {
        findNavController().navigate(R.id.action_groupFragment_to_newGroupFragment)
    }

    private fun navigateToChat(roomId: String) {
        val bundle = Bundle()
        bundle.putString(ROOM_ID, roomId)
        findNavController().navigate(R.id.action_groupFragment_to_chatFragment, bundle)
    }


    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }
}