package com.kml.holders

import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kml.models.Work
import kotlinx.android.synthetic.main.list_item_work_history.view.*

class WorkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var lastClick: Long = 0

    fun bind(work: Work, onClickListener: (Work) -> Unit) {
        itemView.history_work_item_name.text = work.workName
        itemView.history_work_item_time.text = work.executionTime
        itemView.history_work_item_date.text = work.workDate
        itemView.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClick < 500)
                return@setOnClickListener

            onClickListener(work)
            lastClick = SystemClock.elapsedRealtime()
        }
    }
}