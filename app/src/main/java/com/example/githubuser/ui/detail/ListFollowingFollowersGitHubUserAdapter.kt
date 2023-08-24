package com.example.githubuser.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.data.remote.response.FollowingFollowerResponseItem
import com.example.githubuser.databinding.ItemRowGithubUserBinding

class ListFollowingFollowersGitHubUserAdapter(private val listFollowingFollowersGitHubUserAdapter: List<FollowingFollowerResponseItem>) :
    RecyclerView.Adapter<ListFollowingFollowersGitHubUserAdapter.ListViewHolder>() {

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

    override fun getItemCount(): Int = listFollowingFollowersGitHubUserAdapter.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val avatarUrl = listFollowingFollowersGitHubUserAdapter[position].avatarUrl
        val login = listFollowingFollowersGitHubUserAdapter[position].login
        Glide.with(holder.itemView)
            .load(avatarUrl)
            .apply(RequestOptions().override(100, 100))
            .into(holder.binding.imgAvatar)
        holder.binding.tvUsername.text = login
    }
}