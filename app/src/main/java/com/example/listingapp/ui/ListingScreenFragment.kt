package com.example.listingapp.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import com.example.listingapp.R
import com.example.listingapp.adapter.UserAdapter
import com.example.listingapp.databinding.ListingScreenFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.listing_screen_fragment.view.*

class ListingScreenFragment : Fragment() {

    private lateinit var binding: ListingScreenFragmentBinding
    private lateinit var viewModel: ListingScreenViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.listing_screen_fragment,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val viewModelFactory = ListingScreenViewModelFactory(application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ListingScreenViewModel::class.java)

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        if (checkNetwork(requireContext())) {
            viewModel.refreshTheData()
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()

        val manager = GridLayoutManager(activity, 2)
        binding.usersList.layoutManager = manager
        adapter = UserAdapter(UserAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
            Log.v("bala", it.toString())
        })
        binding.usersList.adapter = adapter

        viewModel.resultList.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.v("bala", it.toString())
                adapter.submitList(it)
            }
        })

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController()
                    .navigate(ListingScreenFragmentDirections.actionListingScreenFragmentToUserDetailFragment(
                        it))
                viewModel.displayPropertyDetailsComplete()
            }
        })
        viewModel.weatherProperties.observe(viewLifecycleOwner, Observer {
            binding.currentLocationTesxtView.text = viewModel.weatherProperties.value?.name.toString()
        })
        viewModel.weatherProperties.observe(viewLifecycleOwner, Observer {
            val temp = viewModel.weatherProperties.value?.main?.temp?.toDouble()
            viewModel.getCelsius(temp = temp)
        })
        viewModel.weatherProperties.observe(viewLifecycleOwner, Observer {
            binding.hummidityInCurrLocation.text =
                viewModel.weatherProperties.value?.main?.humidity.toString()
        })
        viewModel.weatherProperties.observe(viewLifecycleOwner, Observer {
            binding.tempInCurrLocation.text = viewModel.celsius.value.toString()
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    private fun checkNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

        private fun getCurrentLocation() {
        if (checkPermissions()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()){ task ->
                    val location : Location? = task.result
                    if (location != null){
                        val latitude = location.latitude
                        val longitude = location.longitude
                        Log.v("bala2","${location.latitude},${location.longitude}")
                        getWeather(latitude,longitude)
                    }
                }
            }else{
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermission()
        }
    }

    private fun isLocationEnabled():Boolean{
        Log.v("bala","is loc enabled")
        val locationManager : LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val res = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
        if (res){
            return res
        }
        Log.v("bala","is loc enabled is false")
        return false
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }
    private fun checkPermissions():Boolean{
        Log.v("bala","checkpermission")
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }
    private fun requestPermission() {
        Log.v("bala","Req per")
        requestPermissions(
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.v("bala","Req per result")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }
        }
    }
    private fun getWeather(latitude: Double, longitude: Double) {

        val apiKey = "d514387db5e5459c9f3f0380837ea34c"
        val lat = latitude.toString()
        val lon = longitude.toString()
        Log.v("bala",lat)
        Log.v("bala",lon)
        viewModel.getWeatherDetails(lat,lon,apiKey)

    }


}



