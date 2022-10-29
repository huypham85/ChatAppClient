package com.vn.chat_app_client.presentation.chats

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.model.Message

private const val VIEW_TYPE_MY_MESSAGE = 1
private const val VIEW_TYPE_OTHER_MESSAGE = 2

class MessageAdapter(val context: Context) : RecyclerView.Adapter<MessageViewHolder>() {
    private var messages: List<Message> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        return if (message.senderId == 1) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == VIEW_TYPE_MY_MESSAGE) {
            MyMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.my_message, parent, false)
            )
        } else {
            OtherMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.other_message, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.bind(message)
    }

    inner class MyMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.findViewById(R.id.txtMyMessage)
        override fun bind(message: Message) {
            messageText.text = message.message

        }
    }

    inner class OtherMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.findViewById(R.id.txtOtherMessage)

        override fun bind(message: Message) {
            messageText.text = message.message
        }
    }
}

open class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(message: Message) {}
}