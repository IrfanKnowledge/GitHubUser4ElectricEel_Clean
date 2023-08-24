package com.example.githubuser.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.githubuser.R

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val dummyQuerySearch = "irfan"
    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun assertQuerySearch() {
        onView(withId(R.id.search_view_github_users_toolbar_menu)).perform(click()).perform(typeText(dummyQuerySearch))

    }

    @Test
    fun assertRvItemClicked() {
        onData(withId(R.id.item_row_github_user))
            .inAdapterView(withId(R.id.rv_github_users))
            .atPosition(0)
            .perform(click())
    }
}