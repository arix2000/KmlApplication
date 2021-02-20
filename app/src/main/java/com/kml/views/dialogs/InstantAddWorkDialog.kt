package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kml.Constants.Signal
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.data.models.WorkToAdd
import com.kml.databinding.DialogNewWorkInstantBinding
import com.kml.viewModels.WorkTimerViewModel


class InstantAddWorkDialog(private val viewModel: WorkTimerViewModel) : AppDialogs(false) {

    companion object {
        const val TODAY = "Dzisiaj"
    }

    lateinit var binding: DialogNewWorkInstantBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        layoutInflater.inflate(R.layout.dialog_new_work_instant, null)
        binding = DialogNewWorkInstantBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        binding.apply {

            newWorkCreationDate.text = TODAY

            dialogTimerAddInstant.setOnClickListener {
                val creationDateString = viewModel.decideAboutDate(newWorkCreationDate.text.toString())
                val description = " $creationDateString -> " + dialogTimerWorkDescriptionInstant.text.toString()

                val work = WorkToAdd(dialogTimerWorkNameInstant.text.toString(),
                        description,
                        dialogTimerHours.text.toString().toIntOrNull() ?: -1,
                        dialogTimerMinutes.text.toString().toIntOrNull() ?: -1
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

        if (!validation(work))
            return
        dismiss()

        val result = viewModel.sendWorkToDatabase(work)

        if (result) {
            Toast.makeText(requireContext(), R.string.adding_work_confirmation, Toast.LENGTH_SHORT).show()
        } else Toast.makeText(requireContext(), R.string.adding_work_error, Toast.LENGTH_SHORT).show()
    }

    private fun validation(work: WorkToAdd): Boolean {
        val result = viewModel.validateWorkInstant(work)
        return if (result != Signal.VALIDATION_SUCCESSFUL) {
            Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
            false
        } else true

    }


}