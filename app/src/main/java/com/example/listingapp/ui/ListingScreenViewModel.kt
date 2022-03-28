package com.example.listingapp.ui

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.listingapp.WeatherModel
import com.example.listingapp.database.getDatabase
import com.example.listingapp.domain.UserDataModel
import com.example.listingapp.network.ObjectUsersApi
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
    val navigateToSelectedProperty: LiveData<UserDataModel?>
        get() = _navigateToSelectedProperty

    private var _weatherProperties = MutableLiveData<WeatherModel?>()
    val weatherProperties: LiveData<WeatherModel?>
        get() = _weatherProperties

    private var _celsius = MutableLiveData<String>()
    val celsius : LiveData<String>
        get() = _celsius

    fun refreshTheData(){
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

    fun getWeatherDetails(lat: String, lon: String, apiKey: String) {
        coroutineScope.launch {
            Log.v("bala 11","getWeather called")
            val getPropertiesDeferred = ObjectUsersApi.retrofitServiceWeather.getRandomUsersWeatherAsync(lat = lat, lon = lon, appId =apiKey)
            try {
                val listResult = getPropertiesDeferred.await()

                if(listResult.code() == 200) {
                    Log.v("bala 11", listResult.toString())
                    _weatherProperties.value = listResult.body()

                    Log.v("bala 11", _weatherProperties.value.toString())
                }else{
                    Log.v("bala 11", "Crashinggg!")
                }
            } catch (e: Exception) {
                _weatherProperties.value = null
            }
        }
    }
    fun getCelsius(temp: Double?) {
        val temperatureKelvin = temp?.minus(273.15)
        val tempInInt = temperatureKelvin!!.toInt()
        _celsius.value = "$tempInInt deg celsius"
    }

}

