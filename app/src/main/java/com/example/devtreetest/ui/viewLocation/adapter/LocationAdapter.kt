package com.example.devtreetest.ui.viewLocation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.devtreetest.datalayers.data.entity.Place

class LocationAdapter() :
    RecyclerView.Adapter<LocationViewHolder>() {
    private var lstLocation = emptyList<Place>()
    lateinit var editClick: (location: Place) -> Unit
    lateinit var deleteClick: (location: Place) -> Unit
    lateinit var mapClick: () -> Unit
    fun setData(items: List<Place>) {
        lstLocation = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return lstLocation.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        return holder.bind(lstLocation[position], editClick, deleteClick,mapClick)
    }
}

