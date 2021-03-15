package com.kml.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.models.TimeToVolunteers
import com.kml.extensions.log
import com.kml.holders.TimeHolder

class TimeAdapter(private val onClickListener: (TimeToVolunteers) -> Unit,
                  private val onLongClickListener: (TimeToVolunteers) -> Boolean)
    : RecyclerView.Adapter<TimeHolder>() {

    init {
        setHasStableIds(true)
    }

    val times: MutableList<TimeToVolunteers> = arrayListOf()

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
        log(times[time.id].hours+"h "+times[time.id].hours+"min <----> "+time.hours+"h "+time.minutes+"min")
        times[time.id] = time
        notifyItemChanged(time.id)
    }
}