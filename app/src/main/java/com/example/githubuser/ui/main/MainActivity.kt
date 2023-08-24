package com.example.githubuser.ui.main

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.ListSearchViewGitHubUsersAdapter
import com.example.githubuser.ui.detail.DetailGitHubUserActivity
import com.example.githubuser.ui.favorite.FavoriteGithubUsersActivity
import com.example.githubuser.ui.settings.SettingsActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        binding?.apply {
            setContentView(root)

            setSupportActionBar(myToolbar)


            mainViewModel.snackBar.observe(this@MainActivity) {
                it.getContentIfNotHandled()?.let { snackBarText ->
                    Snackbar.make(
                        coordinator,
                        snackBarText,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        showRecyclerView()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_view_github_users_menu, menu)

        val searchManager = getSystemService<SearchManager>()
        val searchView =
            menu?.findItem(R.id.search_view_github_users_toolbar_menu)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager?.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.querry_hint)


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mainViewModel.findGitHubUsers(query)
                    showRecyclerView()
                } ?: Toast.makeText(
                    this@MainActivity,
                    "Kata kunci tidak boleh kosong!",
                    Toast.LENGTH_SHORT
                ).show()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_github_user_menu -> {
                startActivity(Intent().apply {
                    setClass(this@MainActivity, FavoriteGithubUsersActivity::class.java)
                })
            }
            R.id.settings_menu -> {
                startActivity(Intent().apply {
                    setClass(this@MainActivity, SettingsActivity::class.java)
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerView() {
        binding?.apply {
            rvGithubUsers.layoutManager = LinearLayoutManager(this@MainActivity)

            mainViewModel.itemsItem.observe(this@MainActivity) {
                val itemsItem = ArrayList<ItemsItem>()
                itemsItem.addAll(it)
                rvGithubUsers.adapter = ListSearchViewGitHubUsersAdapter(itemsItem).apply {
                    setOnItemClickCallback(object :
                        ListSearchViewGitHubUsersAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ItemsItem) {
                            startActivity(Intent().apply {
                                setClass(this@MainActivity, DetailGitHubUserActivity::class.java)
                                putExtra(DetailGitHubUserActivity.EXTRA_USERNAME, data.login)
                            })
                        }
                    })
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}