package com.vn.chat_app_client.ui.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vn.chat_app_client.databinding.FragmentRecentCallsBinding

class RecentCallsFragment : Fragment() {

    private lateinit var binding: FragmentRecentCallsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }
}