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
import com.example.listingapp.R
import com.example.listingapp.databinding.UserDetailFragmentBinding


class UserDetailFragment : Fragment() {

    private lateinit var binding : UserDetailFragmentBinding
    private lateinit var viewModel: UserDetailViewModel
    private lateinit var apiKey : String
    private lateinit var lat : String
    private lateinit var lon : String


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

        viewModel.properties.observe(viewLifecycleOwner, Observer {
            val cel = viewModel.properties.value?.main?.temp?.toDouble()
            viewModel.getCelsius(cel)
        })

        viewModel.properties.observe(viewLifecycleOwner, Observer {
            binding.windSpeedTextView.text = viewModel.properties.value?.wind?.speed.toString()
        })

        viewModel.properties.observe(viewLifecycleOwner, Observer {
            binding.tempTextView.text = viewModel.celsius.value.toString()
        })

        viewModel.properties.observe(viewLifecycleOwner, Observer {
            binding.humidityTextView.text = viewModel.properties.value?.main?.humidity.toString()
        })

        viewModel.properties.observe(viewLifecycleOwner, Observer {
            binding.weatherTextView.text = viewModel.properties.value?.weather?.get(0)?.description.toString()
        })

        return binding.root
    }
    private fun getWeather(){
        apiKey = "d514387db5e5459c9f3f0380837ea34c"
        lat = viewModel.selectedProperty.value?.latitude.toString()
        lon = viewModel.selectedProperty.value?.longitude.toString()
        Log.v("bala",lat)
        Log.v("bala",lon)
        viewModel.getWeatherDetails(lat,lon,apiKey)
    }

}
