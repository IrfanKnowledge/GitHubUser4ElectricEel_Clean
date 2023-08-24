package com.example.githubuser.ui.favorite

import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.ui.GithubUserRepositoryViewModelFactory
import com.example.githubuser.ui.detail.DetailGitHubUserActivity
import com.example.githubuser.ui.detail.DetailGitHubUserViewModel
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito.mock

class FavoriteGithubUsersViewModelTest {
    private lateinit var favoriteGithubUsersViewModel: FavoriteGithubUsersViewModel
    private lateinit var favoriteGithubUsersActivity: FavoriteGithubUsersActivity

    private lateinit var detailGitHubUserViewModel: DetailGitHubUserViewModel
    private lateinit var detailGitHubUserActivity: DetailGitHubUserActivity

    private val dummyGithubUser: FavoriteUser = FavoriteUser("sidiqpermana", "https://avatars.githubusercontent.com/u/4090245?v=4")

    @Before
    fun before() {
        favoriteGithubUsersActivity = mock(FavoriteGithubUsersActivity::class.java)
        favoriteGithubUsersViewModel = ViewModelProvider(
            favoriteGithubUsersActivity.viewModelStore,
            GithubUserRepositoryViewModelFactory.getInstance(favoriteGithubUsersActivity.application)
        )[FavoriteGithubUsersViewModel::class.java]

        detailGitHubUserActivity = mock(DetailGitHubUserActivity::class.java)
        detailGitHubUserViewModel = ViewModelProvider(
            detailGitHubUserActivity.viewModelStore,
            GithubUserRepositoryViewModelFactory.getInstance(detailGitHubUserActivity.application)
        )[DetailGitHubUserViewModel::class.java]
    }

    @Test
    fun getAllFavoriteUsers() {
        favoriteGithubUsersActivity = FavoriteGithubUsersActivity()
        favoriteGithubUsersViewModel = ViewModelProvider(
            favoriteGithubUsersActivity.viewModelStore,
            GithubUserRepositoryViewModelFactory.getInstance(favoriteGithubUsersActivity.application)
        )[FavoriteGithubUsersViewModel::class.java]

        detailGitHubUserActivity = DetailGitHubUserActivity()
        detailGitHubUserViewModel = ViewModelProvider(
            detailGitHubUserActivity.viewModelStore,
            GithubUserRepositoryViewModelFactory.getInstance(detailGitHubUserActivity.application)
        )[DetailGitHubUserViewModel::class.java]

        detailGitHubUserViewModel.deleteUserFavorite(dummyGithubUser)
        detailGitHubUserViewModel.insertUserFavorite(dummyGithubUser)

        val list = favoriteGithubUsersViewModel.getAllFavoriteUsers().value
        var user = FavoriteUser()
        if (list?.isEmpty() == false) {
            for (item in list) {
                if (item.username == dummyGithubUser.username) {
                    user = item
                    break
                }
            }
        }
        assertEquals(dummyGithubUser.username, user.username)
    }
}