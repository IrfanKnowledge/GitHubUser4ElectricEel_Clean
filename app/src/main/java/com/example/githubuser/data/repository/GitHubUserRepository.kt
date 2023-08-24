package com.example.githubuser.data.repository

import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.local.room.FavoriteUserDao

class GitHubUserRepository private constructor(
    private val favoriteUserDao: FavoriteUserDao
) {
    fun getAllFavoriteUsers() = favoriteUserDao.getAllFavoriteUsers()

    suspend fun isUserExists(username: String) =
        favoriteUserDao.isUserExists(username)

    suspend fun insertUserFavorite(favoriteUser: FavoriteUser) {
        favoriteUserDao.insert(favoriteUser)
    }

    suspend fun deleteUserFavorite(favoriteUser: FavoriteUser) {
        favoriteUserDao.delete(favoriteUser)
    }

    companion object {
        @Volatile
        private var instance: GitHubUserRepository? = null
        fun getGitHubUserRepository(favoriteUserDao: FavoriteUserDao): GitHubUserRepository =
            instance ?: synchronized(this) {
                instance ?: GitHubUserRepository(favoriteUserDao)
            }.also { instance = it }
    }
}