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

    private val adapter: RoomAdapter by lazy {
        RoomAdapter(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        binding.rcvRoom.layoutManager = LinearLayoutManager(context)
        binding.rcvRoom.adapter = adapter


        lifecycleScope.launchWhenStarted {
            viewModel.messageReceivedFlow.collect{
                Toast.makeText(context,it.text,Toast.LENGTH_LONG).show()
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