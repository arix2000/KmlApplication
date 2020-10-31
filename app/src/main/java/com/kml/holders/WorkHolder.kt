package com.kml.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kml.data.models.Work
import kotlinx.android.synthetic.main.list_item_work_history.view.*

class WorkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(work: Work, onClickListener: (Work) -> Unit)
    {
        itemView.history_work_item_name.text = work.workName
        itemView.history_work_item_description.text = work.workDescription
        itemView.history_work_item_date.text = work.workDate
        itemView.setOnClickListener { onClickListener(work) }
    }

}