package com.kml.views.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.data.listeners.OnResultListener
import kotlinx.android.synthetic.main.dialog_date_picker.view.*

class MyDatePickerDialog: AppDialogs() {

    lateinit var onResultListener: OnResultListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_date_picker, null)
        builder.setView(view)

        view.apply {
            dialog_date_picker_accept.setOnClickListener {
                val datePicker = dialog_date_picker_picker
                val result = ""+datePicker.dayOfMonth+"."+datePicker.month+"."+datePicker.year
                onResultListener.onReceive(result)
                dismiss()
            }

            dialog_date_picker_cancel.setOnClickListener { dismiss() }
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