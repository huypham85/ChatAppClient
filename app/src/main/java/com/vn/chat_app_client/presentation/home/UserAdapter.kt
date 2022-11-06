package com.vn.chat_app_client.presentation.home

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.databinding.ItemUserBinding
import com.vn.chat_app_client.utils.extensions.viewBinding

class UserAdapter(val listener: UserClickListener) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var users: List<User> = emptyList()

    interface UserClickListener {
        fun onClickUser(userId: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = parent.viewBinding(ItemUserBinding::inflate)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.onClickItem(user)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.tvUsername).text = user.username
        }

        fun onClickItem(user: User) {
            binding.tvUsername.setOnClickListener {
                listener.onClickUser(user.id)
            }
        }
    }
}

