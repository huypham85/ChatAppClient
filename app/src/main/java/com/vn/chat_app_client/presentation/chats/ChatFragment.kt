package com.vn.chat_app_client.presentation.chats

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.databinding.FragmentChatBinding
import com.vn.chat_app_client.presentation.MainActivity
import com.vn.chat_app_client.utils.RealPathUtil
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
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
                // Callback is invo                                                                                                                                                                                                                                                                                                                                                   ked after the user selects media items or closes the
                // photo picker.
                if (uris.isNotEmpty()) {
                    Log.d("PhotoPicker", " item selected: $uris")
                    val attachmentPaths = uris.map {
                        val file = File(it.toString())
//                        URIPathHelper.getRealPathFromURI(requireContext(), it)
                        RealPathUtil.getRealPath(requireContext(), it)
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
            viewModel.sendNewMessage(binding.messageEdt.text.toString().trim())
            binding.messageEdt.setText("")
            binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }

        binding.photoBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
        var roomName = ""
        binding.backBtn.setOnClickListener {
            Toast.makeText(requireContext(),"$roomName and you has left the chat", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }

        viewModel.fetchMessage(arguments)

        lifecycleScope.launchWhenStarted {
            viewModel.messages.collect {
                adapter.reloadData(it)
                binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }

        lifecycleScope.launchWhenStarted {
            try {
                viewModel.messageResponse.collect {
                    viewModel.addNewMessage(it)
                }
            } catch (e: Exception) {
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.roomName.collect {
                binding.roomNameTxt.text = it
                roomName = it
            }
        }

        binding.messageEdt.setOnClickListener { checkKeyboard() }

        lifecycleScope.launchWhenStarted {
            viewModel.enableSend.collect {
                binding.sendBtn.isEnabled = it
            }
        }

        binding.messageEdt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank()) {
                    viewModel.updateSendButton(true)
                } else {
                    viewModel.updateSendButton(false)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //NOOP
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).invisibleBottomNav()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).visibleBottomNav()
    }
}