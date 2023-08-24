package com.example.githubuser.data.local.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SettingPreferencesViewModelFactory(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingPreferencesViewModel::class.java)) {
            return SettingPreferencesViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}