package com.example.githubuser.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.di.Injection
import com.example.githubuser.data.repository.GitHubUserRepository
import com.example.githubuser.ui.detail.DetailGitHubUserViewModel
import com.example.githubuser.ui.favorite.FavoriteGithubUsersViewModel

class GithubUserRepositoryViewModelFactory private constructor(private val gitHubUserRepository: GitHubUserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteGithubUsersViewModel::class.java)) {
            return FavoriteGithubUsersViewModel(gitHubUserRepository) as T
        } else if (modelClass.isAssignableFrom(DetailGitHubUserViewModel::class.java)) {
            return DetailGitHubUserViewModel(gitHubUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: GithubUserRepositoryViewModelFactory? = null

        fun getInstance(context: Context): GithubUserRepositoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: GithubUserRepositoryViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}