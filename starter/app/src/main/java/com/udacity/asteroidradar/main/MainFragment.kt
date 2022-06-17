package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Filters
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.MainViewModel.Factory
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            Factory(requireActivity().application)
        ).get(MainViewModel::class.java)
    }

    private val asteroidAdapter = AsteroidAdapter(AsteroidAdapter.AdapterClickListener {
        viewModel.onAsteroidClicked(it)
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.asteroidRecycler.adapter = asteroidAdapter

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            it.apply {
                asteroidAdapter.submitList(this)
            }
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.onAsteroidNavigated()
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onFilterChanged(
            when (item.itemId) {
                R.id.show_rent_menu -> {
                    Filters.TODAY
                }
                R.id.show_all_menu -> {
                    Filters.WEEK
                }
                else -> {
                    Filters.ALL
                }
            }
        )
        return true
    }
}
