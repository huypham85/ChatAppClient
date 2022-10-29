package com.vn.chat_app_client.presentation.chats

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.model.Message
import com.vn.chat_app_client.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: FragmentChatBinding

    private val adapter: MessageAdapter by lazy {
        MessageAdapter(requireContext())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding.viewModel = viewModel

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chatRecyclerView.adapter = adapter

        binding.sendBtn.setOnClickListener {
            val message = Message(1, binding.messageEdt.text.toString(), 1)
            viewModel.addNewMessage(message)
            binding.messageEdt.setText("")
            binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.messages.collect {
                adapter.reloadData(it)
            }
        }

        binding.messageEdt.setOnClickListener { checkKeyboard() }

        return binding.root
    }

    private fun checkKeyboard() {
        val view = binding.rootView
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val r = Rect()
                view.getWindowVisibleDisplayFrame(r)
                val heightDiff = view.rootView.height - r.height()
                if (heightDiff > 0.25 * view.rootView.height - r.height()) {
                    if (adapter.itemCount > 0) {
                        binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }
        })
    }

}