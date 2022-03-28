package com.example.listingapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.listingapp.database.UsersDatabase
import com.example.listingapp.database.asDomainModel
import com.example.listingapp.domain.UserDataModel
import com.example.listingapp.network.ObjectUsersApi
import com.example.listingapp.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRepository (private val database: UsersDatabase) {

    val userData: LiveData<List<UserDataModel>> = Transformations.map(database.userDataDao.getUser()) {
        it.asDomainModel()
    }


    suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            val result = ObjectUsersApi.retrofitService.getPropertiesAsync().await().body()
            Log.v("bala refreshing data",result.toString())
            if (result != null) {
                database.userDataDao.insertAll(*result.asDatabaseModel())
            }
        }
    }

}