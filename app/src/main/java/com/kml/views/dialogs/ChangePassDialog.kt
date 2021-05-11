package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.kml.Constants.Signal.VALIDATION_SUCCESSFUL
import com.kml.R
import com.kml.views.BaseDialog
import com.kml.data.networking.DbChangePass
import com.kml.databinding.DialogChangePassBinding
import com.kml.extensions.showToast
import com.kml.viewModels.ProfileViewModel

class ChangePassDialog(private val viewModel: ProfileViewModel) : BaseDialog(false) {

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
        if (validationResult != VALIDATION_SUCCESSFUL) {
            showToast(validationResult)
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
        showToast(result)
        binding.dialogChangePasswordProgressBar.visibility = View.GONE
    }
}