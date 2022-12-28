package com.example.devtreetest.ui.map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.devtreetest.R
import com.example.devtreetest.core.Constant
import com.example.devtreetest.databinding.FragmentMapBinding
import com.example.devtreetest.datalayers.data.DatabaseBuilder
import com.example.devtreetest.mapUtil.model.Direction
import com.example.devtreetest.mapUtil.model.Route
import com.example.devtreetest.mapUtil.util.DirectionConverter
import com.example.devtreetest.ui.map.viewmodel.MapViewModel
import com.example.devtreetest.utils.Status
import com.example.devtreetest.utils.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {

    private lateinit var viewModel: MapViewModel
    private lateinit var binding: FragmentMapBinding
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment).getMapAsync { googleMap ->
            this.googleMap = googleMap
            viewModel.fetchLocations()
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                DatabaseBuilder.getInstance(requireContext())
            )
        )[MapViewModel::class.java]

        viewModel.getLocations().observe(viewLifecycleOwner) {

            if(it.isNotEmpty()) {
              if(it.size >= 2) {
                  viewModel.requestDirection(Constant.SERVER_KEY)
              } else {
                  val latLng = LatLng(it[0].latitude!!, it[0].longitude!!)
                  googleMap?.addMarker(MarkerOptions().position(latLng))
              }
            }
        }
        viewModel.getDirection().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    onDirectionSuccess(it.data)
                }
                Status.LOADING -> {
                    showToast(getString(R.string.loading))
                }
                Status.ERROR -> {
                    showToast(it.message)
                }
            }
        }
    }


    private fun onDirectionSuccess(direction: Direction?) {
        direction?.let {
            if (direction.isOK) {
                val route = direction.routeList[0]

                val legCount = route.legList.size
                for (index in 0 until legCount) {
                    val leg = route.legList[index]
                    googleMap?.addMarker(MarkerOptions().position(leg.startLocation.coordination))
                    if (index == legCount - 1) {
                        googleMap?.addMarker(MarkerOptions().position(leg.endLocation.coordination))
                    }
                    val stepList = leg.stepList
                    val polylineOptionList = DirectionConverter.createTransitPolyline(
                        requireContext(),
                        stepList,
                        5,
                        Color.RED,
                        3,
                        Color.BLUE
                    )
                    for (polylineOption in polylineOptionList) {
                        googleMap?.addPolyline(polylineOption)
                    }
                }
                setCameraWithCoordinationBounds(route)
            } else {
                showToast(direction.status)
            }
        } ?: run {
        }
    }

    private fun setCameraWithCoordinationBounds(route: Route) {
        val southwest = route.bound.southwestCoordination.coordination
        val northeast = route.bound.northeastCoordination.coordination
        val bounds = LatLngBounds(southwest, northeast)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    private fun showToast(message: String?) {
        message?.let {
            Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
        }
    }
    companion object {

        @JvmStatic
        fun getInstance(): MapFragment {
            return MapFragment()
        }
    }
}