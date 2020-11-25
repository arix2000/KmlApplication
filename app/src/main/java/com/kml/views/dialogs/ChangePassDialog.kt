package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.data.externalDbOperations.DbChangePass
import com.kml.databinding.DialogChangePassBinding
import com.kml.viewModels.ProfileViewModel

class ChangePassDialog(private val viewModel: ProfileViewModel) : AppDialogs(false) {

    lateinit var binding: DialogChangePassBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        binding = DialogChangePassBinding.inflate(layoutInflater)

        builder.setView(binding.root)

        binding.apply {
            btnDialogChangePassAccept.setOnClickListener {
                changePass(dialogOldPassword.text.toString(), dialogNewPassword.text.toString())
            }
            btnDialogChangePassCancel.setOnClickListener { dismiss() }
        }

        return builder.create()
    }

    private fun changePass(oldPassword: String, newPassword: String) {

        val validationResult = viewModel.validatePassword(oldPassword, newPassword)
        if (validationResult != ProfileViewModel.VALIDATION_SUCCESSFUL) {
            Toast.makeText(requireContext(), validationResult, Toast.LENGTH_SHORT).show()
            return
        }

        binding.dialogChangePasswordProgressBar.visibility = View.VISIBLE
        val changeOperation = viewModel.resolvePasswordChanging(newPassword, oldPassword)

        changeOperation.setOnResultListener {
            resolveResult(it)
        }
    }

    private fun resolveResult(result: String) {
        if (result == DbChangePass.CHANGE_SUCCESSFUL) {
            dismiss()
        }
        binding.dialogOldPassword.setText("")
        binding.dialogNewPassword.setText("")
        Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
        binding.dialogChangePasswordProgressBar.visibility = View.GONE
    }
}