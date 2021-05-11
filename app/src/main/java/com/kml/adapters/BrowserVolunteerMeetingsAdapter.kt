package com.kml.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kml.Constants.Strings.NO_TYPE
import com.kml.R
import com.kml.holders.WorkHolder
import com.kml.models.Work

class BrowserVolunteerMeetingsAdapter(private val onClickListener: (Work) -> Unit) :
    RecyclerView.Adapter<WorkHolder>() {
    private var worksConstant: List<Work> = listOf()
    private var works: List<Work> = listOf()

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

    fun updateWorks(newWorks: List<Work>, initialSet:Boolean = false) {
        if (!initialSet) works = newWorks
        if (worksConstant.isEmpty())
            worksConstant = newWorks
        notifyDataSetChanged()
    }

    fun filterWorksBy(type: String, typeList: List<String>) {
        val filteredList = worksConstant.filter {
            if (typeList.contains(it.type))
                type == it.type
            else type == NO_TYPE
        }
        updateWorks(filteredList)
    }

    fun isWorksEmpty(): Boolean = works.isEmpty()
}