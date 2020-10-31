package com.kml.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.data.models.Work
import com.kml.holders.WorkHolder
import java.util.*

class WorkAdapter(private val onClickListener: (Work) -> Unit)
    : RecyclerView.Adapter<WorkHolder>() {

    var works: List<Work> = ArrayList()
        set(value) {
            notifyDataSetChanged()
            field = value
        }
    lateinit var progressBar: ProgressBar

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