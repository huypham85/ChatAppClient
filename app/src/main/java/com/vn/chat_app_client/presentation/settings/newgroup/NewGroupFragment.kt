package com.vn.chat_app_client.presentation.settings.newgroup

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
import com.vn.chat_app_client.databinding.FragmentNewGroupBinding
import com.vn.chat_app_client.presentation.home.HomeFragment
import com.vn.chat_app_client.presentation.home.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewGroupFragment : Fragment() {

    private lateinit var binding: FragmentNewGroupBinding
    private val viewModel: NewGroupViewModel by viewModels()

    private val chooseUserAdapter: ChooseUserAdapter by lazy {
        val listener = object : ChooseUserAdapter.ChooseUserClickListener {
            override fun onClickUser(userId: String) {
                viewModel.unChooseUser(userId)
            }
        }
        ChooseUserAdapter(listener)
    }

    private val userAdapter: UserAdapter by lazy {
        val listener = object : UserAdapter.UserClickListener {
            override fun onClickUser(userId: String) {
                viewModel.chooseUser(userId)
            }
        }
        UserAdapter(listener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNewGroupBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        binding.rcvUser.layoutManager = LinearLayoutManager(context)
        binding.rcvUser.adapter = userAdapter

        binding.rcvChooseUser.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvChooseUser.adapter = chooseUserAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.listUserShow.collect {
                userAdapter.reloadData(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.listChooseUserShow.collect {
                chooseUserAdapter.reloadData(it)
                binding.ivAdd.visibility = (if (it.isNotEmpty()) View.VISIBLE else View.GONE)
            }
        }

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.event.collect { event ->
                    when (event) {
                        is NewGroupViewModel.Event.NavigateToChat -> navigateToChat(event.roomId)
                    }
                }
            }
        }

        return binding.root
    }

    private fun navigateToChat(roomId: String) {
        val bundle = Bundle()
        bundle.putString(HomeFragment.ROOM_ID, roomId)
        findNavController().navigate(R.id.action_newGroupFragment_to_chatFragment, bundle)
    }

}