package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.app.AppDialogs
import kotlinx.android.synthetic.main.dialog_restore_from_file.view.*

class MakeSureDialog : AppDialogs() {

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
}