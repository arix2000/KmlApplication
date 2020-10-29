package com.kml.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kml.R

class WorkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    var workHistoryName: TextView = itemView.findViewById(R.id.history_work_name)

    @JvmField
    var workHistoryDescription: TextView = itemView.findViewById(R.id.history_work_description)

    @JvmField
    var workHistoryDate: TextView = itemView.findViewById(R.id.history_work_date)

}