package com.kml.holders

import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.databinding.ListItemWorkHistoryBinding
import com.kml.extensions.gone
import com.kml.extensions.visible
import com.kml.models.dto.Work
import com.kml.models.model.User
import kotlinx.android.synthetic.main.list_item_work_history.view.*

open class WorkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var lastClick: Long = 0
    private val binding = ListItemWorkHistoryBinding.bind(itemView)

    open fun bind(work: Work, onClickListener: (Work) -> Unit, user: User = User.EMPTY) {
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
            if (work.isUserWasOnMeeting(user))
                setEnabledStyle()
            else
                setDisabledStyle()
        }
    }

    private fun setDisabledStyle() {
        binding.apply {
            rootCardView.foreground = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.disablingMask))
            absenceIcon.visible()
        }
    }

    private fun setEnabledStyle() {
        binding.apply {
            rootCardView.foreground = null
            absenceIcon.gone()
        }
    }
}