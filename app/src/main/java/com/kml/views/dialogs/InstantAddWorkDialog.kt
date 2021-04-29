package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.kml.Constants.Numbers.TIME_HAS_NO_VALUE
import com.kml.Constants.Strings.TODAY
import com.kml.Constants.Types.TOAST_TYPE
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.data.utilities.Validator
import com.kml.data.utilities.Vibrator
import com.kml.databinding.DialogNewWorkInstantBinding
import com.kml.extensions.hideSoftKeyboard
import com.kml.extensions.showToast
import com.kml.models.WorkToAdd
import com.kml.viewModels.WorkTimerViewModel


class InstantAddWorkDialog(private val viewModel: WorkTimerViewModel) : AppDialogs(false) {

    lateinit var binding: DialogNewWorkInstantBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext(), R.style.instant_add_work_style)
        layoutInflater.inflate(R.layout.dialog_new_work_instant, null)
        binding = DialogNewWorkInstantBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        binding.apply {
            newWorkCreationDate.text = TODAY
            dialogTimerAddInstant.setOnClickListener {
                requireContext().hideSoftKeyboard(it)
                val creationDateString = viewModel.decideAboutDate(newWorkCreationDate.text.toString())
                val description = " $creationDateString " + dialogTimerWorkDescriptionInstant.text.toString()

                val work = WorkToAdd(dialogTimerWorkNameInstant.text.toString(),
                        description,
                        dialogTimerHours.text.toString().toIntOrNull() ?: TIME_HAS_NO_VALUE,
                        dialogTimerMinutes.text.toString().toIntOrNull() ?: TIME_HAS_NO_VALUE
                )
                sendWorkToDatabase(work)
            }

            newWorkCreationDate.setOnClickListener {
                val dialog = MyDatePickerDialog()
                dialog.setOnResultListener {
                    newWorkCreationDate.text = it
                }
                dialog.show(parentFragmentManager, "DatePicker")
            }

            dialogTimerCancelInstant.setOnClickListener {
                dismiss()
            }
        }
        return builder.create()
    }

    private fun sendWorkToDatabase(work: WorkToAdd) {
        if (!Validator(requireActivity(), TOAST_TYPE).validateWork(work))
            return

        binding.worksProgressBar.visibility = View.VISIBLE
        viewModel.sendWorkToDatabase(work) {
            binding.worksProgressBar.visibility = View.GONE
            if (it) {
                requireContext().showToast(getString(R.string.adding_work_confirmation))
                dismiss()
                Vibrator(requireContext()).longVibrate()
            } else showToast(R.string.adding_work_error)
        }
    }



}