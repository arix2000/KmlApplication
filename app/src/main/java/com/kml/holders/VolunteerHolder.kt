package com.kml.holders

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.kml.data.models.Volunteer
import kotlinx.android.synthetic.main.list_item_volunteer.view.*

class VolunteerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(volunteer: Volunteer, onClickListener: (Volunteer) -> Unit) {

        val fullName = volunteer.firstName + " " + volunteer.lastName
        itemView.apply {
            volunteer_item_name.text = fullName
            volunteer_item_checkbox.isChecked = volunteer.isChecked
            //TODO set Enabled on volunteer to false if not belongs to actual time
            setOnClickListener {
                setCheckboxState(volunteer_item_checkbox)
                onClickListener(volunteer)
            }
        }
    }

    private fun setCheckboxState(checkBox: CheckBox) {
        checkBox.isChecked = !checkBox.isChecked
    }
}