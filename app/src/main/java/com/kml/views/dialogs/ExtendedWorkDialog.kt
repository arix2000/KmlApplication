package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kml.Constants.Tags.MEETINGS_TAG
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.databinding.DialogWorkHistoryExtendedBinding
import com.kml.extensions.gone
import com.kml.extensions.visible
import com.kml.models.Work


class ExtendedWorkDialog(val work: Work, val type: String, private val shouldShowVolunteers: Boolean = false) : AppDialogs() {

    lateinit var binding: DialogWorkHistoryExtendedBinding
    var isVolunteersExpanded = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        binding = DialogWorkHistoryExtendedBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        binding.apply {
            dialogHistoryWorkName.text = work.workName
            dialogHistoryWorkDescription.text = work.workDescription
            dialogHistoryWorkDate.text = work.workDate
            dialogHistoryExecutionTime.text = work.executionTime
            dialogHistoryVolunteers.text = work.people?.trim()?.removeSuffix(",")
            setOrHideFullName()
            showOrHideVolunteersInfo()
            setExpanding()
        }
        return builder.create()
    }

    private fun setOrHideFullName() {
        with(work) {
            binding.apply {
                val fullName = "$firstName $lastName"
                if (firstName.isNullOrBlank() || lastName.isNullOrBlank()) {
                    fullNameTitle.gone()
                    dialogHistoryFullName.gone()
                } else {
                    fullNameTitle.visible()
                    dialogHistoryFullName.visible()
                    dialogHistoryFullName.text = fullName
                }
            }
        }
    }

    private fun setExpanding() {
        binding.dialogHistoryVolunteers.apply {
            setOnClickListener {
                maxLines = if (isVolunteersExpanded) 2 else Int.MAX_VALUE
                isVolunteersExpanded = !isVolunteersExpanded
            }
        }
    }

    private fun showOrHideVolunteersInfo() {
        binding.apply {
            if (shouldShowVolunteers && type == MEETINGS_TAG) {
                dialogHistoryVolunteers.visible()
                volunteersTitle.visible()
            } else {
                dialogHistoryVolunteers.gone()
                volunteersTitle.gone()
            }
        }
    }

}