package com.example.listingapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity.apply
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.SearchViewBindingAdapter.setOnQueryTextListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager

import com.example.listingapp.R
import com.example.listingapp.adapter.UserAdapter
import com.example.listingapp.databinding.ListingScreenFragmentBinding
import kotlinx.android.synthetic.main.listing_screen_fragment.view.*

class ListingScreenFragment : Fragment() {

    private lateinit var binding:ListingScreenFragmentBinding
    private lateinit var viewModel: ListingScreenViewModel
    private lateinit var adapter: UserAdapter


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
        viewModel = ViewModelProvider(this,viewModelFactory).get(ListingScreenViewModel::class.java)

        binding.lifecycleOwner = this

        if(checkNetwork(requireContext())){
            viewModel.refreshTheData()
        }

        val manager = GridLayoutManager(activity, 2)
        binding.usersList.layoutManager = manager
         adapter = UserAdapter(UserAdapter.OnClickListener{
            viewModel.displayPropertyDetails(it)
        })
        binding.usersList.adapter = adapter

        viewModel.resultList.observe(viewLifecycleOwner, Observer{
            it?.let {
                Log.v("bala", it.toString())
                adapter.submitList(it)
            }
        })

//        val searchView : android.widget.SearchView = binding.searchView
//        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return true
//            }
//
//        })

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                this.findNavController().navigate(ListingScreenFragmentDirections.actionListingScreenFragmentToUserDetailFragment(it))
                viewModel.displayPropertyDetailsComplete()
            }
        })

        return binding.root
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

}