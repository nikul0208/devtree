package com.example.devtreetest.ui.addLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.devtreetest.core.Constant
import com.example.devtreetest.databinding.FragmentAddLocationBinding
import com.example.devtreetest.datalayers.data.DatabaseBuilder
import com.example.devtreetest.ui.addLocation.adapter.AutoCompleteAdapter
import com.example.devtreetest.ui.addLocation.viewmodel.AddLocationModel
import com.example.devtreetest.utils.Status
import com.example.devtreetest.utils.ViewModelFactory
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient


class AddLocationFragment : Fragment() {

    private lateinit var viewModel: AddLocationModel
    private lateinit var binding: FragmentAddLocationBinding

    private var adapter: AutoCompleteAdapter? = null
    private lateinit var placesClient: PlacesClient

    private var locationId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddLocationBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            locationId = it.getInt("locationId")
        }
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                DatabaseBuilder.getInstance(requireContext())
            )
        )[AddLocationModel::class.java]


        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), Constant.SERVER_KEY)
        }

        placesClient = Places.createClient(requireContext())
        initAutoCompleteTextView()

        viewModel.isPlaceAdded().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val bundle = Bundle()
                    bundle.putString("action", "addOrEditLocation");
                    this.parentFragmentManager.setFragmentResult("result", bundle)
                    activity?.onBackPressed()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        }

    }

    private fun initAutoCompleteTextView() {
        binding.auto.threshold = 1
        binding.auto.onItemClickListener = autocompleteClickListener
        adapter = AutoCompleteAdapter(requireContext(), placesClient)
        binding.auto.setAdapter(adapter)
    }
    private val autocompleteClickListener =
        AdapterView.OnItemClickListener { adapterView, view, i, l ->
            try {
                val item = adapter!!.getItem(i)
                viewModel.fetchPlaceRequest(locationId,item.placeId,placesClient)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    companion object {



        @JvmStatic
        fun getInstance(locationId: Int): AddLocationFragment {

            val bundle = Bundle()
            bundle.putInt("locationId", locationId)

            val fragment = AddLocationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}