package com.example.devtreetest.ui.viewLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devtreetest.R
import com.example.devtreetest.databinding.FragmentViewLocationBinding
import com.example.devtreetest.datalayers.data.DatabaseBuilder
import com.example.devtreetest.ui.addLocation.AddLocationFragment
import com.example.devtreetest.ui.map.MapFragment
import com.example.devtreetest.ui.viewLocation.adapter.LocationAdapter
import com.example.devtreetest.ui.viewLocation.viewmodel.ViewLocationViewModel
import com.example.devtreetest.utils.ViewModelFactory


class ViewLocationFragment : Fragment() {

    private lateinit var viewModel: ViewLocationViewModel
    private lateinit var binding: FragmentViewLocationBinding
    private lateinit var adapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentViewLocationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.parentFragmentManager.setFragmentResultListener(
            "result", this
        ) { _, bundle ->
            val action = bundle.getString("action")
            if (action != null) {
                if (action == "addOrEditLocation") {
                    viewModel.fetchLocations()
                }
            }
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                DatabaseBuilder.getInstance(requireContext())
            )
        )[ViewLocationViewModel::class.java]

        adapter = LocationAdapter()
        binding.rvLocations.adapter = adapter
        binding.rvLocations.layoutManager = LinearLayoutManager(requireContext())
        binding.btnAddLocation.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.home_container, AddLocationFragment.getInstance(0))
                ?.addToBackStack("addLocationFragment")
                ?.commitAllowingStateLoss()
        }

        adapter.editClick = {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.home_container, AddLocationFragment.getInstance(it.id!!))
                ?.addToBackStack("addLocationFragment")
                ?.commitAllowingStateLoss()
        }

        adapter.deleteClick = {
            viewModel.deleteLocation(it)
        }

        adapter.mapClick = {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.home_container, MapFragment.getInstance())
                ?.addToBackStack("mapFragment")
                ?.commitAllowingStateLoss()
        }
        binding.btnAsce.setOnClickListener {
            viewModel.sortBy(1)
        }

        binding.btnDesc.setOnClickListener {
            viewModel.sortBy(2)
        }
        viewModel.getLocations().observe(viewLifecycleOwner) {
            it?.let { items ->
                adapter.setData(items)
            }
        }
    }

    companion object {

        @JvmStatic
        fun getInstance(): ViewLocationFragment {
            return ViewLocationFragment()
        }
    }
}