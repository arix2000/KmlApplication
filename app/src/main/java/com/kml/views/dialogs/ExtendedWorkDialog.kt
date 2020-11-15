package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.data.models.Work
import kotlinx.android.synthetic.main.dialog_work_history_extended.view.*

class ExtendedWorkDialog(val work: Work) : AppDialogs() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        val view = layoutInflater.inflate(R.layout.dialog_work_history_extended, null)
        builder.setView(view)

        view.apply {
            dialog_history_work_name.text = work.workName
            dialog_history_work_description.text = work.workDescription
            dialog_history_work_date.text = work.workDate
            dialog_history_execution_time.text = work.executionTime
        }

        return builder.create()
    }

}