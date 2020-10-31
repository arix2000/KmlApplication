package com.kml.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.data.models.Volunteer
import com.kml.holders.VolunteerHolder

class VolunteerAdapter(private val onClickListener: (Volunteer) -> Unit)
    : RecyclerView.Adapter<VolunteerHolder>() {
    var volunteers: List<Volunteer> = listOf()
        set(value) {
            notifyDataSetChanged(); field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteerHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_volunteer, parent, false)
        return VolunteerHolder(itemView)
    }

    override fun onBindViewHolder(holder: VolunteerHolder, position: Int) {
        val volunteer = volunteers[position]
        holder.bind(volunteer, onClickListener)
    }

    override fun getItemCount(): Int {
        return volunteers.size
    }
}