package com.vn.chat_app_client.presentation.chats

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.databinding.FragmentChatBinding
import com.vn.chat_app_client.utils.URIPathHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: FragmentChatBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    @Inject
    lateinit var savedAccountManager: SavedAccountManager
    private val adapter: MessageAdapter by lazy {
        MessageAdapter(requireContext(), savedAccountManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                // Callback is invoked after the user selects media items or closes the
                // photo picker.
                if (uris.isNotEmpty()) {
                    Log.d("PhotoPicker", " item selected: $uris")
                    val attachmentPaths = uris.map {
//                        URIPathHelper.getRealPathFromURI(requireContext(), it)
                        it.path
                    }
                    attachmentPaths[0]?.let { Log.d("Photo Path", it) }
                    viewModel.addPhotoMessages(attachmentPaths)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
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
            viewModel.sendNewMessage(binding.messageEdt.text.toString())
            binding.messageEdt.setText("")
            binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }

        binding.photoBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        viewModel.fetchMessage(arguments)

        lifecycleScope.launchWhenStarted {
            viewModel.messages.collect {
                adapter.reloadData(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.messageResponse.collect {
                viewModel.addMessage(it)
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