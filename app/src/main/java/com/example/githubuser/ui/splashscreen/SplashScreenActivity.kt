package com.example.githubuser.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.githubuser.R
import com.example.githubuser.data.local.datastore.SettingPreferences
import com.example.githubuser.data.local.datastore.SettingPreferencesViewModel
import com.example.githubuser.data.local.datastore.SettingPreferencesViewModelFactory
import com.example.githubuser.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val settingPreferencesViewModel by viewModels<SettingPreferencesViewModel> {
        val pref = SettingPreferences.getInstance(dataStore)
        SettingPreferencesViewModelFactory(pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        settingPreferencesViewModel.getThemeSettings()
            .observe(this@SplashScreenActivity) {isDarkModeActive ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

            }

        lifecycleScope.launch {
            delay(3000)
            startActivity(Intent().apply {
                setClass(this@SplashScreenActivity, MainActivity::class.java)
            })
            finish()
        }
    }
}