package com.vn.chat_app_client.presentation.settings.newgroup

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.databinding.ItemChooseUserBinding
import com.vn.chat_app_client.utils.extensions.viewBinding

class ChooseUserAdapter(val listener: ChooseUserClickListener) :
    RecyclerView.Adapter<ChooseUserAdapter.ChooseUserViewHolder>() {
    private var users: List<User> = emptyList()

    interface ChooseUserClickListener {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseUserViewHolder {
        val binding = parent.viewBinding(ItemChooseUserBinding::inflate)
        return ChooseUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseUserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.onClickItem(user)
    }

    inner class ChooseUserViewHolder(private val binding: ItemChooseUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvUsername.text = user.username
        }

        fun onClickItem(user: User) {
            binding.ivClose.setOnClickListener {
                listener.onClickUser(user.id)
            }
        }
    }
}

