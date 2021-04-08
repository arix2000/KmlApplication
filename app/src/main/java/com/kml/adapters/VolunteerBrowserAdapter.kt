package com.kml.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.holders.VolunteerBrowserHolder
import com.kml.models.Volunteer

class VolunteerBrowserAdapter(
        private val itemClick: (Volunteer) -> Unit
) : RecyclerView.Adapter<VolunteerBrowserHolder>() {
    private var volunteers: List<Volunteer> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteerBrowserHolder {
        return VolunteerBrowserHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_volunteer_browser, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VolunteerBrowserHolder, position: Int) {
        val volunteer = volunteers[position]
        holder.bind(volunteer, itemClick)
    }

    fun updateData(data: List<Volunteer>){
        volunteers = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = volunteers.size
}