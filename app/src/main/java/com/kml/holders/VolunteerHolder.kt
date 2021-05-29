package com.kml.holders

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.databinding.ListItemVolunteerBinding
import com.kml.models.dto.Volunteer

class VolunteerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding = ListItemVolunteerBinding.bind(itemView)

    fun bind(volunteer: Volunteer, onClickListener: (Volunteer) -> Unit) {

        val fullName = volunteer.firstName + " " + volunteer.lastName
        binding.run {
            volunteerItemName.text = fullName
            volunteerItemCheckbox.isChecked = volunteer.isChecked

            root.run {
                isClickable = !volunteer.isDisabled
                isFocusable = !volunteer.isDisabled
                isEnabled = !volunteer.isDisabled
            }

            root.setOnClickListener {
                setCheckboxState(volunteerItemCheckbox, !volunteer.isChecked)
                onClickListener(volunteer)
            }

            if (!volunteer.isDisabled) {
                setEnabledStyle()
            }
            else setDisabledStyle()
        }
    }

    private fun setCheckboxState(checkBox: CheckBox, isActivated: Boolean) {
        checkBox.isChecked = isActivated
    }

    private fun setDisabledStyle() {
        binding.apply {
            root.foreground = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.disablingMask))
            volunteerItemCheckbox.isChecked = true
        }
    }

    private fun setEnabledStyle() {
        binding.apply {
            root.foreground = null
        }
    }
}