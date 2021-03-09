package com.kml.holders

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.kml.data.models.Volunteer
import com.kml.databinding.ListItemVolunteerBinding

class VolunteerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding = ListItemVolunteerBinding.bind(itemView)

    fun bind(volunteer: Volunteer, onClickListener: (Volunteer) -> Unit) {

        val fullName = volunteer.firstName + " " + volunteer.lastName
        binding.run {
            volunteerItemName.text = fullName
            volunteerItemCheckbox.isChecked = volunteer.isChecked

            root.setOnClickListener {
                setCheckboxState(volunteerItemCheckbox, !volunteer.isChecked)
                onClickListener(volunteer)
            }
        }
    }

    private fun setCheckboxState(checkBox: CheckBox, isActivated: Boolean) {
        checkBox.isChecked = isActivated
    }

}