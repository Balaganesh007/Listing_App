package com.example.listingapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.listingapp.domain.UserDataModel
import org.json.JSONException

class UserDetailViewModel(userDataModel: UserDataModel, application: Application) : AndroidViewModel(application) {

    private var requestQueue: RequestQueue? = null

    private var _temp = MutableLiveData<String>()
    val temp : LiveData<String>
        get() = _temp

    private var _humidity = MutableLiveData<String>()
    val humidity : LiveData<String>
        get() = _humidity

    private var _windSpeed = MutableLiveData<String>()
    val windSpeed : LiveData<String>
        get() = _windSpeed

    private var _weatherDes = MutableLiveData<String>()
    val weatherDes : LiveData<String>
        get() = _weatherDes

    private var _selectedProperty = MutableLiveData<UserDataModel>()
    val selectedProperty : LiveData<UserDataModel>
        get() = _selectedProperty


    init {
        _selectedProperty.value = userDataModel
        requestQueue = Volley.newRequestQueue(this.getApplication())
    }



    fun getWeatherDetails(url: String) {
        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {
            val jsonArray = response.getJSONArray("weather")
            val jsonObject = response.getJSONObject("main")
            val jsonObject2 = response.getJSONObject("wind")

            val temp : String = jsonObject.getString("temp")
            val humidity = jsonObject.getString("humidity")
            val windSpeed = jsonObject2.getString("speed")

             _temp.value = temp.toString()
            _humidity.value = humidity.toString()
            _windSpeed.value = windSpeed.toString()

            for (i in 0 until jsonArray.length()) {
                val weather = jsonArray.getJSONObject(i)
                val weatherDescription = weather.getString("description")
                val weatherDes = weatherDescription.toString()
                _weatherDes.value = weatherDes.toString()

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } }, { error -> error.printStackTrace() })
        requestQueue?.add(request)
    }

}