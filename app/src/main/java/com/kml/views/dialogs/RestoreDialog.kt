package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.kml.views.BaseDialog
import com.kml.databinding.DialogRestoreFromFileBinding

class RestoreDialog : BaseDialog(false) {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogRestoreFromFileBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        binding.btnDialogRestoreYes.setOnClickListener {
            dismiss()
            onAcceptListener.onAccept()
        }
        binding.btnDialogRestoreNo.setOnClickListener {
            dismiss()
            onCancelListener.onCancel()
        }

        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}