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

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteerHolder =
            VolunteerHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_volunteer, parent, false))

    override fun onBindViewHolder(holder: VolunteerHolder, position: Int) {
        val volunteer = volunteers[position]
        holder.bind(volunteer, onClickListener)
    }

    fun updateVolunteers(newVolunteers: List<Volunteer>) {
        this.volunteers = newVolunteers
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return volunteers.size
    }
}