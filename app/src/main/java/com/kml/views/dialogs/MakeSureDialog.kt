package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.databinding.DialogRestoreFromFileBinding

class MakeSureDialog : AppDialogs() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogRestoreFromFileBinding.inflate(layoutInflater)

        builder.setView(binding.root)
        binding.apply {
            progressFounded.setText(R.string.warning)
            shouldRestore.setText(R.string.reset_confirmation)

            btnDialogRestoreYes.setText(R.string.reset)
            btnDialogRestoreYes.setOnClickListener {
                dismiss()
                onAcceptListener.onAccept()
            }

            btnDialogRestoreNo.setText(R.string.cancel)
            btnDialogRestoreNo.setOnClickListener { dismiss() }
        }

        return builder.create()
    }
}