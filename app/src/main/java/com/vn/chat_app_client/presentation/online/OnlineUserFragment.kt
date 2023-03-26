package com.vn.chat_app_client.presentation.online

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vn.chat_app_client.R
import com.vn.chat_app_client.databinding.FragmentNewGroupBinding
import com.vn.chat_app_client.databinding.FragmentOnlineUserBinding
import com.vn.chat_app_client.presentation.home.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OnlineUserFragment : Fragment() {
    private lateinit var binding: FragmentOnlineUserBinding
    private val viewModel: OnlineUserViewModel by viewModels()

    private val onlineUserAdapter: OnlineUserAdapter by lazy {
        val listener = object : OnlineUserAdapter.UserClickListener {
            override fun onClickUser(userId: String) {
//                viewModel.chooseUser(userId)
            }
        }
        OnlineUserAdapter(listener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOnlineUserBinding.inflate(layoutInflater,container,false)

        binding.onlineRcv.layoutManager = LinearLayoutManager(requireContext())
        binding.onlineRcv.adapter = onlineUserAdapter
        viewModel.getOnlineUsers()
        lifecycleScope.launchWhenStarted {
            viewModel.listUserShow.collect {
                onlineUserAdapter.reloadData(it)
            }
        }
        return binding.root
    }
}