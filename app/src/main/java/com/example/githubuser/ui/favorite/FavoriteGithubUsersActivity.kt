package com.example.githubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.ActivityFavoriteGithubUsersBinding
import com.example.githubuser.ui.GithubUserRepositoryViewModelFactory
import com.example.githubuser.ui.ListSearchViewGitHubUsersAdapter
import com.example.githubuser.ui.detail.DetailGitHubUserActivity
import com.google.android.material.snackbar.Snackbar

class FavoriteGithubUsersActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteGithubUsersBinding? = null
    private val binding get() = _binding

    private val favoriteGithubUsersViewModel by viewModels<FavoriteGithubUsersViewModel> {
        GithubUserRepositoryViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteGithubUsersBinding.inflate(layoutInflater)

        binding?.apply {
            setContentView(root)

            myToolbar.title = getString(R.string.favorite_github_user_title)
            setSupportActionBar(myToolbar)

            favoriteGithubUsersViewModel.snackBar.observe(this@FavoriteGithubUsersActivity) {
                it.getContentIfNotHandled()?.let { snackBarText ->
                    Snackbar.make(
                        coordinator,
                        snackBarText,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        showRecyclerView()
    }

    private fun showRecyclerView() {
        binding?.apply {
            rvGithubUsers.layoutManager = LinearLayoutManager(this@FavoriteGithubUsersActivity)

            favoriteGithubUsersViewModel.getAllFavoriteUsers()
                .observe(this@FavoriteGithubUsersActivity) { users ->
                    if (users.isEmpty()) {
                        favoriteGithubUsersViewModel.setSnackBarListIsEmpty()
                    }

                    val items = arrayListOf<ItemsItem>()
                    users.map {
                        val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl ?: "")
                        items.add(item)
                    }

                    progressBar.visibility = View.GONE

                    rvGithubUsers.adapter = ListSearchViewGitHubUsersAdapter(items).apply {
                        setOnItemClickCallback(object :
                            ListSearchViewGitHubUsersAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: ItemsItem) {
                                startActivity(Intent().apply {
                                    setClass(
                                        this@FavoriteGithubUsersActivity,
                                        DetailGitHubUserActivity::class.java
                                    )
                                    putExtra(DetailGitHubUserActivity.EXTRA_USERNAME, data.login)
                                })
                            }
                        })
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}