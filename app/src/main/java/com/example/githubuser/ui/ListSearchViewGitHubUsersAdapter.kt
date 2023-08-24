package com.example.githubuser.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.ItemRowGithubUserBinding

class ListSearchViewGitHubUsersAdapter(private val listSearchViewGitHubUsers: ArrayList<ItemsItem>) :
    RecyclerView.Adapter<ListSearchViewGitHubUsersAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: ItemRowGithubUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(
            ItemRowGithubUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = listSearchViewGitHubUsers.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val avatarUrl = listSearchViewGitHubUsers[position].avatarUrl
        val login = listSearchViewGitHubUsers[position].login
        Glide.with(holder.itemView)
            .load(avatarUrl)
            .apply(RequestOptions().override(100, 100))
            .into(holder.binding.imgAvatar)
        holder.binding.tvUsername.text = login

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSearchViewGitHubUsers[position])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}