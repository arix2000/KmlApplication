package com.kml.holders

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.kml.data.models.TimeToVolunteers
import kotlinx.android.synthetic.main.list_item_work_time.view.*

class TimeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(time: TimeToVolunteers, onClickListener: (TimeToVolunteers) -> Unit,
             onLongClickListener: (TimeToVolunteers) -> Boolean) {
        val hours = itemView.item_time_hours
        val minutes = itemView.item_time_minutes
        hours.setText(time.hours)
        minutes.setText(time.minutes)
        itemView.item_work_number_of_volunteers.text = time.volunteers.size.toString()
        itemView.setOnLongClickListener { onLongClickListener(time) }
        itemView.item_time_button.setOnClickListener { onClickListener(time) }

        hours.doAfterTextChanged { time.hours = it.toString() }
        minutes.doAfterTextChanged { time.minutes = it.toString() }

    }
}