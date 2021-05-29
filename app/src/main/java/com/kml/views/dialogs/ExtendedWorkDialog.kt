package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kml.Constants.Tags.MEETINGS_TAG
import com.kml.R
import com.kml.databinding.DialogWorkHistoryExtendedBinding
import com.kml.extensions.goneAll
import com.kml.extensions.visibleAll
import com.kml.models.dto.Work
import com.kml.views.BaseDialog


class ExtendedWorkDialog(
    val work: Work,
    val type: String,
    val shouldShowFullName: Boolean = false
) : BaseDialog() {

    lateinit var binding: DialogWorkHistoryExtendedBinding
    var isVolunteersExpanded = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        binding = DialogWorkHistoryExtendedBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        binding.apply {
            workName.text = work.workName
            workDescription.text = work.workDescription
            workDate.text = work.workDate
            executionTime.text = work.executionTime
            volunteers.text = work.people?.trim()?.removeSuffix(",")
            workType.text = if (work.type.isNullOrBlank()) resources.getString(R.string.no_type) else work.type
            setOrHideFullName()
            showOrHideVolunteersInfo()
            showOrHideFullNames()
            setExpanding()
        }
        return builder.create()
    }

    private fun setOrHideFullName() {
        with(work) {
            binding.apply {
                val fullName = "$firstName $lastName"
                if (firstName.isNullOrBlank() || lastName.isNullOrBlank()) {
                    goneAll(fullNameTitle, dialogHistoryFullName)
                } else {
                    visibleAll(fullNameTitle, dialogHistoryFullName)
                    dialogHistoryFullName.text = fullName
                }
            }
        }
    }

    private fun setExpanding() {
        binding.volunteers.apply {
            setOnClickListener {
                maxLines = if (isVolunteersExpanded) 2 else Int.MAX_VALUE
                isVolunteersExpanded = !isVolunteersExpanded
            }
        }
    }

    private fun showOrHideVolunteersInfo() {
        binding.apply {
            if (type == MEETINGS_TAG)
                visibleAll(volunteers, volunteersTitle, workType, workTypeTitle)
             else
                goneAll(volunteers, volunteersTitle, workType, workTypeTitle)

        }
    }

    private fun showOrHideFullNames() {
        binding.apply {
            if (type == MEETINGS_TAG && shouldShowFullName)
                visibleAll(fullNameTitle,dialogHistoryFullName)
            else
                goneAll(fullNameTitle,dialogHistoryFullName)

        }
    }
}