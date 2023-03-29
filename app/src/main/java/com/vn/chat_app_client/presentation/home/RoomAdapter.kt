package com.vn.chat_app_client.presentation.home

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vn.chat_app_client.data.api.common.Consts
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.model.Room
import com.vn.chat_app_client.databinding.ItemRoomBinding
import com.vn.chat_app_client.utils.extensions.toDate
import com.vn.chat_app_client.utils.extensions.toDateView

import com.vn.chat_app_client.utils.extensions.viewBinding

class RoomAdapter(val listener: RoomClickListener,val savedAccountManager: SavedAccountManager) :
    RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    private var rooms: List<Room> = emptyList()

    interface RoomClickListener {
        fun onClickRoom(roomId: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData(rooms: List<Room>) {

        this.rooms = rooms.sortedByDescending {
            it.lastMessage?.createdAt?.toDate(Consts.TIME_SERVER_PATTERN)?.time
        }

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding = parent.viewBinding(ItemRoomBinding::inflate)
        return RoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
        holder.onClickItem(room)
    }

    inner class RoomViewHolder(val binding: ItemRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(room: Room) {
            try {
                binding.tvRoomName.text = room.name
                room.lastMessage?.let {
                    if (room.name.split(",").size >= 2) {
                        if (savedAccountManager.fetchUserId() == room.lastMessage.senderId) {
                            binding.tvLastMessage.text = "You: ${room.lastMessage.text}"
                        }else{
                            binding.tvLastMessage.text = "Your friend: ${room.lastMessage.text}"
                        }
                    } else {
                        binding.tvLastMessage.text = room.lastMessage.text
                    }
                }

                binding.tvTime.text =
                    room.lastMessage?.createdAt?.toDate(Consts.TIME_SERVER_PATTERN)
                        ?.toDateView(Consts.HOUR_PATTERN)
            } catch (e: Exception) {
                print(e)
            }
        }

        fun onClickItem(room: Room) {
            binding.root.setOnClickListener {
                listener.onClickRoom(room.id)
            }
        }
    }
}

