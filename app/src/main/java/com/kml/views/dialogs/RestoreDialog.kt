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
import kotlinx.android.synthetic.main.dialog_restore_from_file.view.*

class RestoreDialog : TimerDialogs() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_restore_from_file, null)
        builder.setView(view)

        view.btn_dialog_restore_yes.setOnClickListener {
            dismiss()
            onAcceptListener.onAccept()
        }
        view.btn_dialog_restore_no.setOnClickListener {
            dismiss()
            onCancelListener.onCancel()
        }

        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}