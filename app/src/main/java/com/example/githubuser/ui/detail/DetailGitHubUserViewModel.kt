package com.example.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.remote.response.DetailGithubUserResponse
import com.example.githubuser.data.remote.response.FollowingFollowerResponseItem
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.data.repository.GitHubUserRepository
import com.example.githubuser.helper.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class DetailGitHubUserViewModel(private val repository: GitHubUserRepository) : ViewModel() {
    private val _detailGithubUserResponse = MutableLiveData<DetailGithubUserResponse>()
    val detailGithubUserResponse: LiveData<DetailGithubUserResponse> = _detailGithubUserResponse

    private val _followingFollowerResponseItem =
        MutableLiveData<List<FollowingFollowerResponseItem>>()
    val followingFollowerResponseItem: LiveData<List<FollowingFollowerResponseItem>> =
        _followingFollowerResponseItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackBar = MutableLiveData<Event<String>>()
    val snackBar: LiveData<Event<String>> = _snackBar

    private fun vOnFailure(message: String) = Timber.d("onFailure: $message")

    fun findDetailGitHubUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailGithubUserResponse?> {
            override fun onResponse(
                call: Call<DetailGithubUserResponse?>,
                response: Response<DetailGithubUserResponse?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _detailGithubUserResponse.value = it
                    }
                } else {
                    vOnFailure(response.message())
                    _snackBar.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<DetailGithubUserResponse?>, t: Throwable) {
                _isLoading.value = false
                vOnFailure(t.message ?: "")
                _snackBar.value = Event(t.message ?: "No Response")
            }
        })
    }

    fun findFollowingSomeGitHubUser(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowingFromUser(q)
        requestFollowingFollower(client)
    }

    fun findFollowersSomeGitHubUser(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowersFromUser(q)
        requestFollowingFollower(client)
    }

    private fun requestFollowingFollower(client: Call<List<FollowingFollowerResponseItem>>) {
        client.enqueue(object : Callback<List<FollowingFollowerResponseItem>?> {
            override fun onResponse(
                call: Call<List<FollowingFollowerResponseItem>?>,
                response: Response<List<FollowingFollowerResponseItem>?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _followingFollowerResponseItem.value = it
                        if (it.isEmpty()) _snackBar.value = Event("Tidak ada data")
                    }
                } else {
                    vOnFailure(response.message())
                    _snackBar.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<List<FollowingFollowerResponseItem>?>, t: Throwable) {
                _isLoading.value = false
                vOnFailure(t.message ?: "")
                _snackBar.value = Event(t.message ?: "No Response")
            }
        })
    }

    suspend fun isUserExists(username: String) =
        repository.isUserExists(username)

    fun insertUserFavorite(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            repository.insertUserFavorite(favoriteUser)
        }
    }

    fun deleteUserFavorite(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            repository.deleteUserFavorite(favoriteUser)
        }
    }
}