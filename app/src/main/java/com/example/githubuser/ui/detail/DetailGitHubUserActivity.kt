package com.example.githubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.R
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.databinding.ActivityDetailGitHubUserBinding
import com.example.githubuser.ui.GithubUserRepositoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailGitHubUserActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_USERNAME = "EXTRA_DETAIL_GITHUB_USER"

        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private var _binding: ActivityDetailGitHubUserBinding? = null
    private val binding get() = _binding

    private val detailGitHubUserViewModel by viewModels<DetailGitHubUserViewModel> {
        GithubUserRepositoryViewModelFactory.getInstance(application)
    }

    private var favoriteUser: FavoriteUser = FavoriteUser()
    private var isFavoriteUser: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailGitHubUserBinding.inflate(layoutInflater)

        binding?.apply {
            setContentView(root)

            detailGitHubUserViewModel.snackBar.observe(this@DetailGitHubUserActivity) {
                it.getContentIfNotHandled()?.let { snackBarText ->
                    Snackbar.make(
                        coordinator,
                        snackBarText,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        detailGitHubUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        showDetailGithubUser()
        showFollowingFollower()
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.apply {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showDetailGithubUser() {
        intent.getStringExtra(EXTRA_USERNAME)?.let { detailGitHubUserViewModel.findDetailGitHubUser(it) }

        detailGitHubUserViewModel.detailGithubUserResponse.observe(this) { user ->
            binding?.apply {
                Glide.with(this@DetailGitHubUserActivity)
                    .load(user.avatarUrl)
                    .apply(RequestOptions().override(200, 200))
                    .into(imgAvatar)
                tvName.text = user.name
                tvUsername.text = user.login
                tvFollowingValue.text = user.following.toString()
                tvFollowersValue.text = user.followers.toString()

                favoriteUser.avatarUrl = user.avatarUrl
                favoriteUser.username = user?.login ?: ""

                lifecycleScope.launch {
                    val isExists = async {
                        detailGitHubUserViewModel.isUserExists(user.login)
                    }
                    isFavoriteUser = if (isExists.await()) {
                        fabAddFavoriteGithubUser.setImageResource(R.drawable.ic_favorite_full_white_24)
                        fabAddFavoriteGithubUser.setOnClickListener(this@DetailGitHubUserActivity)
                        true
                    } else {
                        fabAddFavoriteGithubUser.setImageResource(R.drawable.ic_favorite_border_white_24)
                        fabAddFavoriteGithubUser.setOnClickListener(this@DetailGitHubUserActivity)
                        false
                    }
                }
            }
        }
    }

    private fun showFollowingFollower() {
        detailGitHubUserViewModel.detailGithubUserResponse.observe(this) {
            binding?.apply {
                viewPager2.adapter = SectionsPagerAdapter(this@DetailGitHubUserActivity).apply {
                    username = it.login
                }
                TabLayoutMediator(tabs, viewPager2) { tab, position ->
                    tab.text = resources.getString(TAB_TITLES[position])
                }.attach()
            }
        }
    }

    override fun onClick(view: View?) {
        binding?.apply {
            if (view?.id == fabAddFavoriteGithubUser.id) {
                isFavoriteUser = if (isFavoriteUser) {
                    detailGitHubUserViewModel.deleteUserFavorite(favoriteUser)
                    fabAddFavoriteGithubUser.setImageResource(R.drawable.ic_favorite_border_white_24)
                    false
                } else {
                    detailGitHubUserViewModel.insertUserFavorite(favoriteUser)
                    fabAddFavoriteGithubUser.setImageResource(R.drawable.ic_favorite_full_white_24)
                    true
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}