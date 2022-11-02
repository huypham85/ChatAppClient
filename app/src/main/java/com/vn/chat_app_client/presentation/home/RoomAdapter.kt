package com.vn.chat_app_client.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.model.Message
import com.vn.chat_app_client.data.model.Room
import com.vn.chat_app_client.databinding.ItemRoomBinding

class RoomAdapter(val context: Context) : RecyclerView.Adapter<RoomViewHolder>() {
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
        return RoomViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_room, parent, false)
        )
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

}

open class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(room: Room) {
        itemView.findViewById<TextView>(R.id.tvRoomName).text = room.id

    }
}