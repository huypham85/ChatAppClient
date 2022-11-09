package com.vn.chat_app_client.presentation.chats

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.api.common.Consts
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.model.MessageType.PHOTO
import com.vn.chat_app_client.data.model.MessageType.TEXT
import com.vn.chat_app_client.data.model.RoomMessage
import com.vn.chat_app_client.utils.extensions.toDateTime

class MessageAdapter(val context: Context, val savedAccountManager: SavedAccountManager) :
    RecyclerView.Adapter<MessageViewHolder>() {

    private var messages: List<RoomMessage> = emptyList()

    companion object {
        const val MY_TEXT_MESSAGE = 1
        const val OTHER_TEXT_MESSAGE = 2
        const val MY_PHOTO_MESSAGE = 3
        const val OTHER_PHOTO_MESSAGE = 4
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData(messages: List<RoomMessage>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        return if (message.senderId == savedAccountManager.fetchUserId()) {
            when (message.type) {
                TEXT -> MY_TEXT_MESSAGE
                PHOTO -> MY_PHOTO_MESSAGE
                null -> MY_TEXT_MESSAGE
            }
        } else {
            when (message.type) {
                TEXT -> OTHER_TEXT_MESSAGE
                PHOTO -> OTHER_PHOTO_MESSAGE
                null -> MY_TEXT_MESSAGE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            MY_TEXT_MESSAGE -> {
                MyMessageViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.my_text_message, parent, false)
                )
            }

            MY_PHOTO_MESSAGE -> {
                MyPhotoMessageViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.my_photo_message, parent, false)
                )
            }

            OTHER_TEXT_MESSAGE -> {
                OtherMessageViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.other_text_message, parent, false)
                )
            }

            else -> {
                OtherPhotoMessageViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.other_photo_message, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.bind(message)
    }

    inner class MyMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.findViewById(R.id.txtMyMessage)
        private var messageTime: TextView = view.findViewById(R.id.txtMyMessageTime)
        override fun bind(message: RoomMessage) {
            messageText.text = message.text
            messageTime.text = message.createdAt.toDateTime()
        }
    }

    inner class OtherMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.findViewById(R.id.txtOtherMessage)
        private var messageTime: TextView = view.findViewById(R.id.txtOtherMessageTime)
        private var messageUsername: TextView = view.findViewById(R.id.txtOtherUser)
        override fun bind(message: RoomMessage) {
            messageText.text = message.text
            messageTime.text = message.createdAt.toDateTime()
            messageUsername.text = message.senderName
        }
    }

    inner class MyPhotoMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageImage: ImageView = view.findViewById(R.id.myPhotoImg)

        override fun bind(message: RoomMessage) {
            Glide.with(context).load("${Consts.BASE_URL}/public/${message.attachments[0]}").into(messageImage)
        }
    }

    inner class OtherPhotoMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageImage: ImageView = view.findViewById(R.id.otherPhotoImg)

        override fun bind(message: RoomMessage) {
//            messageImage.setImageURI(message.photoUri)
        }
    }
}

open class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(message: RoomMessage) {
    }
}