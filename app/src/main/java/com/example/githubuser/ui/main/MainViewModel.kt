package com.example.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.data.remote.response.SearchViewGitHubUsersResponse
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.helper.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainViewModel : ViewModel() {
    private val _itemsItem = MutableLiveData<List<ItemsItem>>()
    val itemsItem: LiveData<List<ItemsItem>> = _itemsItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackBar = MutableLiveData<Event<String>>()
    val snackBar: LiveData<Event<String>> = _snackBar

    companion object {
        private const val defaultQ = "sidiqpermana"
    }

    init {
        findGitHubUsers(defaultQ)
    }

    fun findGitHubUsers(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUsers(q)
        fun vOnFailure(message: String) = Timber.d("onFailure: $message")
        client.enqueue(object : Callback<SearchViewGitHubUsersResponse?> {
            override fun onResponse(
                call: Call<SearchViewGitHubUsersResponse?>,
                response: Response<SearchViewGitHubUsersResponse?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _itemsItem.value = it.items
                        if (it.totalCount <= 0) _snackBar.value =
                            Event("Pengguna tidak dapat ditemukan")
                    }
                } else {
                    vOnFailure(response.message())
                    _snackBar.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<SearchViewGitHubUsersResponse?>, t: Throwable) {
                _isLoading.value = false
                vOnFailure(t.message ?: "")
                _snackBar.value = Event(t.message ?: "No Response")
            }
        })
    }
}