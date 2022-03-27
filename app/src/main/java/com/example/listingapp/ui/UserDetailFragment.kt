package com.example.listingapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.listingapp.R
import com.example.listingapp.databinding.UserDetailFragmentBinding
import org.json.JSONException


class UserDetailFragment : Fragment() {

    private lateinit var binding : UserDetailFragmentBinding
    private lateinit var viewModel: UserDetailViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.user_detail_fragment,
            container,
            false
        )
        val application = requireNotNull(this.activity).application
        val userProperty = UserDetailFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = UserDetailViewModelFactory(userProperty, application)
        binding.userDetailViewModel = ViewModelProvider(this,viewModelFactory).get(UserDetailViewModel::class.java)
        viewModel =  ViewModelProvider(this,viewModelFactory).get(UserDetailViewModel::class.java)

        getWeather()

        viewModel.humidity.observe(viewLifecycleOwner,Observer{
            binding.humidityTextView.text = viewModel.humidity.value.toString()
        })

        viewModel.temp.observe(viewLifecycleOwner, Observer {
            binding.tempTextView.text = viewModel.temp.value.toString()
        })

        viewModel.weatherDes.observe(viewLifecycleOwner, Observer {
            binding.weatherTextView.text = viewModel.weatherDes.value.toString()
        })

        viewModel.windSpeed.observe(viewLifecycleOwner, Observer {
            binding.windSpeedTextView.text = viewModel.windSpeed.value.toString()
        })

        return binding.root
    }
    private fun getWeather(){
        val apiKey = "d514387db5e5459c9f3f0380837ea34c"
        val lat = viewModel.selectedProperty.value?.latitude
        val lon = viewModel.selectedProperty.value?.longitude

        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey"
        viewModel.getWeatherDetails(url)
    }
}
