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

class MakeSureDialog : TimerDialogs() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_restore_from_file, null)
        builder.setView(view)
        view.apply {
            progress_founded.setText(R.string.warning)
            should_restore.setText(R.string.reset_confirmation)
            val btnTak = btn_dialog_restore_yes
            btnTak.setText(R.string.reset)
            btnTak.setOnClickListener {
                dismiss()
                onAcceptListener.onAccept()
            }
            val btnNie = btn_dialog_restore_no
            btnNie.setText(R.string.cancel)
            btnNie.setOnClickListener { dismiss() }
        }

        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}