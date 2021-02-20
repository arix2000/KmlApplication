package com.kml.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.data.models.TimeToVolunteers
import com.kml.holders.TimeHolder

class TimeAdapter(private val onClickListener: (TimeToVolunteers) -> Unit,
                  private val onLongClickListener: (TimeToVolunteers) -> Boolean)
    : RecyclerView.Adapter<TimeHolder>() {

    val times: MutableList<TimeToVolunteers> = arrayListOf(TimeToVolunteers(0, "", "", listOf()))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_work_time, parent, false)

        return TimeHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimeHolder, position: Int) {
        val time = times[position]
        holder.bind(time, onClickListener, onLongClickListener)
    }

    override fun getItemCount(): Int = times.size

    fun addTime(timeToVolunteers: TimeToVolunteers) {
        times.add(timeToVolunteers)
        notifyItemInserted(times.lastIndex)
    }

    fun updateTime(time: TimeToVolunteers) {
        times[time.id] = time
        notifyItemChanged(time.id)
    }
}