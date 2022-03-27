package com.example.listingapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.listingapp.domain.UserDataModel

class UserDetailViewModelFactory (
    private val userDataModel: UserDataModel,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(userDataModel,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}