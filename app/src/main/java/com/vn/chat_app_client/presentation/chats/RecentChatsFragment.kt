package com.vn.chat_app_client.presentation.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vn.chat_app_client.databinding.FragmentRecentChatsBinding

class RecentChatsFragment : Fragment() {

    private lateinit var binding: FragmentRecentChatsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }
}