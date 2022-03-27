package com.example.listingapp.ui

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
import androidx.recyclerview.widget.GridLayoutManager

import com.example.listingapp.R
import com.example.listingapp.adapter.UserAdapter
import com.example.listingapp.databinding.ListingScreenFragmentBinding

class ListingScreenFragment : Fragment() {

    private lateinit var binding:ListingScreenFragmentBinding
    private lateinit var viewModel: ListingScreenViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var searchView: SearchView


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

        //

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                this.findNavController().navigate(ListingScreenFragmentDirections.actionListingScreenFragmentToUserDetailFragment(it))
                viewModel.displayPropertyDetailsComplete()
            }
        })

        return binding.root
    }

}