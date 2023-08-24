package com.example.githubuser.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.githubuser.R
import com.example.githubuser.data.local.datastore.SettingPreferences
import com.example.githubuser.data.local.datastore.SettingPreferencesViewModel
import com.example.githubuser.data.local.datastore.SettingPreferencesViewModelFactory
import com.example.githubuser.databinding.ActivitySettingsBinding
import com.example.githubuser.ui.splashscreen.dataStore

class SettingsActivity : AppCompatActivity() {
    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding

    private val settingPreferencesViewModel by viewModels<SettingPreferencesViewModel> {
        val pref = SettingPreferences.getInstance(dataStore)
        SettingPreferencesViewModelFactory(pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)

        binding?.apply {
            setContentView(root)

            myToolbar.title = getString(R.string.settings)
            setSupportActionBar(myToolbar)

            settingPreferencesViewModel.getThemeSettings()
                .observe(this@SettingsActivity) {isDarkModeActive ->
                    if (isDarkModeActive) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        switchTheme.isChecked = true
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        switchTheme.isChecked = false
                    }
                }

            switchTheme.setOnCheckedChangeListener { _, isChecked ->
                settingPreferencesViewModel.saveThemeSetting(isChecked)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}