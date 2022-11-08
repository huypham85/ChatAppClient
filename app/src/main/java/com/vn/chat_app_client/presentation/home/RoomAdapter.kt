package com.vn.chat_app_client.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vn.chat_app_client.data.model.Message
import com.vn.chat_app_client.data.model.Room
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.databinding.ItemRoomBinding
import com.vn.chat_app_client.utils.extensions.viewBinding

class RoomAdapter(val listener: RoomClickListener) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    private var rooms: List<Room> = emptyList()

    interface RoomClickListener {
        fun onClickRoom(roomId: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData(rooms: List<Room>) {
        this.rooms = rooms
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

    fun receiveNewMessage(message: Message) {
//        if (message.text.isNotBlank())
//            _messages.value.add(message)
//        messageText.value = ""
    }

    inner class RoomViewHolder(val binding: ItemRoomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Room) {
           binding.tvRoomName.text = room.name
        }

        fun onClickItem(room: Room) {
            binding.root.setOnClickListener {
                listener.onClickRoom(room.id)
            }
        }
    }

}

