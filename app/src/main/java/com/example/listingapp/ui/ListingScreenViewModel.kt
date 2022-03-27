package com.example.listingapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.listingapp.database.getDatabase
import com.example.listingapp.domain.UserDataModel
import com.example.listingapp.repository.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListingScreenViewModel(application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val database = getDatabase(application)
    private val usersRepository = UsersRepository(database)

    private val _navigateToSelectedProperty = MutableLiveData<UserDataModel?>()
    val navigateToSelectedProperty: MutableLiveData<UserDataModel?>
        get() = _navigateToSelectedProperty

    init {
        coroutineScope.launch {
            usersRepository.refreshData()
        }
    }

    val resultList = usersRepository.userData

    fun displayPropertyDetails(userDataModel: UserDataModel) {
        _navigateToSelectedProperty.value = userDataModel
    }
    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}