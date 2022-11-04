package com.vn.chat_app_client.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.model.User

class UserAdapter(val context: Context) : RecyclerView.Adapter<UserViewHolder>() {
    private var users: List<User> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

}

open class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(user: User) {
        itemView.findViewById<TextView>(R.id.tvUsername).text = user.username
    }
}