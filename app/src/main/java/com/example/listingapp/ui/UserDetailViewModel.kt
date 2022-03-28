package com.example.listingapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.listingapp.WeatherModel
import com.example.listingapp.domain.UserDataModel
import com.example.listingapp.network.ObjectUsersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserDetailViewModel(userDataModel: UserDataModel,
                          application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private var _selectedProperty = MutableLiveData<UserDataModel>()
    val selectedProperty : LiveData<UserDataModel>
        get() = _selectedProperty

    private var _properties = MutableLiveData<WeatherModel?>()
    val properties: LiveData<WeatherModel?>
        get() = _properties

    private var _celsius = MutableLiveData<String>()
    val celsius : LiveData<String>
    get() = _celsius


    init {
        _selectedProperty.value = userDataModel

    }

    fun getWeatherDetails(lat: String, lon: String, apiKey: String) {
        coroutineScope.launch {
            Log.v("bala","getWeather called")
            val getPropertiesDeferred = ObjectUsersApi.retrofitServiceWeather.getRandomUsersWeatherAsync(lat = lat, lon = lon, appId =apiKey)
            try {
                val listResult = getPropertiesDeferred.await()
                Log.v("bala", listResult.toString())
              _properties.value = listResult.body()
                Log.v("bala", _properties.value.toString())
            } catch (e: Exception) {
                _properties.value = null
            }
        }
    }

    fun getCelsius(temp: Double?) {
        val temperatureKelvin = temp?.minus(273.15)
        val tempInInt = temperatureKelvin!!.toInt()
        _celsius.value = "$tempInInt deg Celsius"
    }
}