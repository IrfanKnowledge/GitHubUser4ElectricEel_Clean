package com.example.githubuser.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return FollowingFollowerFragment().apply {
            arguments = Bundle().apply {
                putString(FollowingFollowerFragment.ARG_USERNAME, username)
                putInt(FollowingFollowerFragment.ARG_POSITION, position)
            }
        }
    }
}