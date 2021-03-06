package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kml.Constants.Date.OLD_DATE_INPUT_FORMAT
import com.kml.Constants.Date.NEW_DATE_OUTPUT_FORMAT
import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.views.BaseDialog
import com.kml.utilities.listeners.OnResultListener
import com.kml.databinding.DialogDatePickerBinding
import java.text.SimpleDateFormat
import java.util.*

class MyDatePickerDialog: BaseDialog() {

    lateinit var onResultListener: OnResultListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogDatePickerBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        binding.apply {
            dialogDatePickerAccept.setOnClickListener {
                val date = dialogDatePickerPicker
                val result = formatDate(date.dayOfMonth.toString()+"."+(date.month+1)+"."+date.year)
                onResultListener.onReceive(result)
                dismiss()
            }

            dialogDatePickerCancel.setOnClickListener { dismiss() }
        }
        return builder.create()
    }

    fun setOnResultListener(operation: (String) -> Unit) {
        onResultListener = object : OnResultListener {
            override fun onReceive(result: String) {
                operation(result)
            }
        }
    }

    private fun formatDate(date: String): String {
        return SimpleDateFormat(NEW_DATE_OUTPUT_FORMAT, Locale.getDefault())
                .format(SimpleDateFormat(OLD_DATE_INPUT_FORMAT, Locale.getDefault()).parse(date) ?: EMPTY_STRING)
    }
}