package com.vn.chat_app_client.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vn.chat_app_client.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val roomAdapter: RoomAdapter by lazy {
        RoomAdapter(requireContext())
    }

    private val userAdapter: UserAdapter by lazy {
        UserAdapter(requireContext())
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
            viewModel.messageReceivedFlow.collect {
                Toast.makeText(context, it.text, Toast.LENGTH_LONG).show()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        lifecycleScope.launchWhenStarted {
//            viewModel.messageReceivedFlow.collect {
//                Toast.makeText(view.context, it.text, Toast.LENGTH_LONG).show()
//            };
//        }
    }


}