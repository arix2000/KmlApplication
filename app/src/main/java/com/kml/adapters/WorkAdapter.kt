package com.kml.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.holders.EmptyWorkHolder
import com.kml.holders.WorkHolder
import com.kml.models.Work
import java.util.*

class WorkAdapter(private val onClickListener: (Work) -> Unit)
    : RecyclerView.Adapter<WorkHolder>() {
    private var worksConstant: List<Work> = listOf()
    private var works: List<Work> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_work_history, parent, false)
        return if (viewType == WORK_VIEW_TYPE) WorkHolder(itemView)
        else EmptyWorkHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkHolder, position: Int) {
        val work = works[position]
        holder.bind(work, onClickListener)
    }

    override fun getItemCount(): Int {
        return works.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (works[position].isEmpty()) EMPTY_WORK_VIEW_TYPE
        else WORK_VIEW_TYPE
    }

    fun updateWorks(newWorks: List<Work>) {
        works = newWorks + EMPTY_WORK
        if (worksConstant.isEmpty())
            worksConstant = newWorks
        notifyDataSetChanged()
    }

    fun filterWorksBy(title: String): List<Work> {
        val filteredWorks = worksConstant.filter {
            it.workName.toLowerCase(Locale.ROOT).contains(title.toLowerCase(Locale.ROOT))
        }
        works = filteredWorks + EMPTY_WORK
        notifyDataSetChanged()
        return filteredWorks
    }

    companion object {
        val EMPTY_WORK = Work("","","","","","","")

        const val EMPTY_WORK_VIEW_TYPE = 1
        const val WORK_VIEW_TYPE = 2
    }
}