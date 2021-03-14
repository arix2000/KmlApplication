package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.data.models.Work
import com.kml.databinding.DialogWorkHistoryExtendedBinding

class ExtendedWorkDialog(val work: Work) : AppDialogs() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        val binding = DialogWorkHistoryExtendedBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        binding.apply {
            dialogHistoryWorkName.text = work.workName
            dialogHistoryWorkDescription.text = work.workDescription
            dialogHistoryWorkDate.text = work.workDate
            dialogHistoryExecutionTime.text = work.executionTime
        }

        return builder.create()
    }

}