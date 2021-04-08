package com.kml.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.holders.WorkHolder
import com.kml.models.Work
import java.util.*

class WorkAdapter(private val onClickListener: (Work) -> Unit)
    : RecyclerView.Adapter<WorkHolder>() {

    var works: List<Work> = ArrayList()
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_work_history, parent, false)
        return WorkHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkHolder, position: Int) {
        val work = works[position]
        holder.bind(work, onClickListener)
    }

    override fun getItemCount(): Int {
        return works.size
    }
}