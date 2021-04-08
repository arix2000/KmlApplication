package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kml.data.app.AppDialogs
import com.kml.data.listeners.OnResultListener
import com.kml.databinding.DialogDatePickerBinding

class MyDatePickerDialog: AppDialogs() {

    lateinit var onResultListener: OnResultListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogDatePickerBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        binding.apply {
            dialogDatePickerAccept.setOnClickListener {
                val date = dialogDatePickerPicker
                val result = ""+date.dayOfMonth+"."+(date.month+1)+"."+date.year
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



}