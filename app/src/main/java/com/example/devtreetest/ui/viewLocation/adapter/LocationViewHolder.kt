package com.example.devtreetest.ui.viewLocation.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.devtreetest.R
import com.example.devtreetest.datalayers.data.entity.Place
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_location.view.*

class LocationViewHolder(override val containerView: View) :

    RecyclerView.ViewHolder(containerView), LayoutContainer {

    companion object {
        fun create(parent: ViewGroup): LocationViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_location, parent, false)
            return LocationViewHolder(view)
        }
    }

    fun bind(
        item: Place,
        editListener: (Place) -> Unit,
        deleteListener: (Place) -> Unit,
        mapListener: () -> Unit
    ) {
        containerView.tvName.text = item.name
        containerView.tvAddress.text = item.address
        containerView.ivDelete.setOnClickListener {
            deleteListener.invoke(item)
        }

        containerView.ivEdit.setOnClickListener {
            editListener.invoke(item)
        }

        containerView.ivMap.setOnClickListener {
            mapListener.invoke()
        }
    }
}
