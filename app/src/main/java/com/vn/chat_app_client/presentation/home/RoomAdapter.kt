package com.vn.chat_app_client.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vn.chat_app_client.data.model.Message
import com.vn.chat_app_client.data.model.Room
import com.vn.chat_app_client.databinding.ItemRoomBinding
import com.vn.chat_app_client.utils.extensions.viewBinding

class RoomAdapter(val context: Context) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    private var rooms: List<Room> = emptyList()

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
        val message = rooms[position]

        holder.bind(message)
    }

    fun receiveNewMessage(message: Message) {
//        if (message.text.isNotBlank())
//            _messages.value.add(message)
//        messageText.value = ""
    }

    open class RoomViewHolder(val binding: ItemRoomBinding) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(room: Room) {
           binding.tvRoomName.text = room.name

        }
    }

}

