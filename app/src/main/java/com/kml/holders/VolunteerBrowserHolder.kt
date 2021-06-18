package com.kml.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.databinding.ListItemVolunteerBrowserBinding
import com.kml.models.dto.Volunteer

class VolunteerBrowserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ListItemVolunteerBrowserBinding.bind(itemView)

    fun bind(volunteer: Volunteer, onClickListener: (Volunteer) -> Unit) {
        binding.apply {
            volunteerName.text = getFullNameString(volunteer)
            root.setOnClickListener {
                onClickListener(volunteer)
            }
        }
    }

    private fun getFullNameString(volunteer: Volunteer) = binding.root.resources
            .getString(R.string.volunteer_full_name, volunteer.firstName, volunteer.lastName)
}