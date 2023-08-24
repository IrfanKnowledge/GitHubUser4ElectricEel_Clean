package com.example.githubuser.data.di

import android.content.Context
import com.example.githubuser.data.local.room.FavoriteUserRoomDatabase
import com.example.githubuser.data.repository.GitHubUserRepository

object Injection {
    fun provideRepository(context: Context): GitHubUserRepository {
        val database = FavoriteUserRoomDatabase.getDatabase(context)
        val dao = database.favoriteUserDao()
        return GitHubUserRepository.getGitHubUserRepository(dao)
    }
}