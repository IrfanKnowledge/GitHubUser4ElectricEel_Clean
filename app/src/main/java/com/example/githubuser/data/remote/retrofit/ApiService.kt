package com.example.githubuser.data.remote.retrofit

import com.example.githubuser.data.remote.response.DetailGithubUserResponse
import com.example.githubuser.data.remote.response.FollowingFollowerResponseItem
import com.example.githubuser.data.remote.response.SearchViewGitHubUsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") q: String
    ): Call<SearchViewGitHubUsersResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") user: String
    ): Call<DetailGithubUserResponse>

    @GET("users/{q}/following")
    fun getFollowingFromUser(
        @Path("q") q: String
    ): Call<List<FollowingFollowerResponseItem>>

    @GET("users/{q}/followers")
    fun getFollowersFromUser(
        @Path("q") q: String
    ): Call<List<FollowingFollowerResponseItem>>
}