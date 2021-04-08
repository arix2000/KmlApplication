package com.kml.holders

import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kml.databinding.ListItemWorkHistoryBinding
import com.kml.models.Work
import kotlinx.android.synthetic.main.list_item_work_history.view.*

class WorkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var lastClick: Long = 0
    private val binding = ListItemWorkHistoryBinding.bind(itemView)

    fun bind(work: Work, onClickListener: (Work) -> Unit) {

        binding.apply {
            historyWorkItemName.text = work.workName
            historyWorkItemTime.text = work.executionTime
            historyWorkItemDate.text = work.workDate
            root.setOnClickListener {
                if (SystemClock.elapsedRealtime() - lastClick < 500)
                    return@setOnClickListener

                onClickListener(work)
                lastClick = SystemClock.elapsedRealtime()
            }
        }
    }
}