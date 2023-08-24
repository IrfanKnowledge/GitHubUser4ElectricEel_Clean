package com.example.githubuser.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.remote.response.FollowingFollowerResponseItem
import com.example.githubuser.databinding.FragmentFollowingFollowerBinding
import com.example.githubuser.ui.GithubUserRepositoryViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FollowingFollowerFragment : Fragment() {

    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }

    private lateinit var binding: FragmentFollowingFollowerBinding
    private val detailGitHubUserViewModel by viewModels<DetailGitHubUserViewModel> {
        GithubUserRepositoryViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowingFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME) ?: ""
        val position = arguments?.getInt(ARG_POSITION, 0)
        if (position == 0) {
            detailGitHubUserViewModel.findFollowingSomeGitHubUser(username)
        } else {
            detailGitHubUserViewModel.findFollowersSomeGitHubUser(username)
        }

        detailGitHubUserViewModel.snackBar.observe(requireActivity()) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    binding.coordinator,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        detailGitHubUserViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        showRecyclerView()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView() {
        binding.rvGithubUsers.layoutManager = LinearLayoutManager(requireContext())

        detailGitHubUserViewModel.followingFollowerResponseItem.observe(requireActivity()) {
            val itemsItem = ArrayList<FollowingFollowerResponseItem>()
            itemsItem.addAll(it)
            binding.rvGithubUsers.adapter = ListFollowingFollowersGitHubUserAdapter(itemsItem)
        }
    }
}