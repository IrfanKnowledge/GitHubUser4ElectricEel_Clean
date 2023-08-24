package com.example.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.repository.GitHubUserRepository
import com.example.githubuser.helper.Event

class FavoriteGithubUsersViewModel(private val repository: GitHubUserRepository) : ViewModel() {
    private val _snackBar = MutableLiveData<Event<String>>()
    val snackBar: LiveData<Event<String>> = _snackBar

    fun setSnackBarListIsEmpty(){
        _snackBar.value = Event("Tidak ada pengguna favorit")
    }

    fun getAllFavoriteUsers() = repository.getAllFavoriteUsers()
}