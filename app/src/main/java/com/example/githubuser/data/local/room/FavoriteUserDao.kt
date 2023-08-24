package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.data.local.entity.FavoriteUser

// DDL = Data Definition Language
@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteUser: FavoriteUser)

    @Delete
    suspend fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * from favorite_user ORDER BY username ASC")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_user WHERE username = :username)")
    suspend fun isUserExists(username: String): Boolean
}