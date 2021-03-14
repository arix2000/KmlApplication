package com.kml.holders

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.kml.data.models.TimeToVolunteers
import com.kml.databinding.ListItemWorkTimeBinding

class TimeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding: ListItemWorkTimeBinding = ListItemWorkTimeBinding.bind(itemView)

    fun bind(time: TimeToVolunteers, onClickListener: (TimeToVolunteers) -> Unit,
             onLongClickListener: (TimeToVolunteers) -> Boolean) {

        binding.apply {
            itemTimeHours.setText(time.hours)
            itemTimeMinutes.setText(time.minutes)
            itemWorkNumberOfVolunteers.text = time.volunteers.size.toString()
            root.setOnLongClickListener { onLongClickListener(time) }
            itemTimeButton.setOnClickListener { onClickListener(time) }

            itemTimeHours.doAfterTextChanged { time.hours = it.toString() }
            itemTimeMinutes.doAfterTextChanged { time.minutes = it.toString() }
        }

    }
}