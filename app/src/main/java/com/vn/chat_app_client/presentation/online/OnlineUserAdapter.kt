package com.vn.chat_app_client.presentation.online

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.databinding.OnlineItemBinding
import com.vn.chat_app_client.utils.extensions.viewBinding

class OnlineUserAdapter(val listener: UserClickListener) :
    RecyclerView.Adapter<OnlineUserAdapter.OnlineUserViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineUserViewHolder {
        val binding = parent.viewBinding(OnlineItemBinding::inflate)
        return OnlineUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnlineUserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.onClickItem(user)
    }

    inner class OnlineUserViewHolder(private val binding: OnlineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvUsername.text = user.username
        }

        fun onClickItem(user: User) {
            binding.root.setOnClickListener {
                listener.onClickUser(user.id)
            }
        }
    }
}
