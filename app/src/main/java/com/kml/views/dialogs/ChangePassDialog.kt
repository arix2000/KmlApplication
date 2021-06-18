package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kml.Constants.Signal.VALIDATION_SUCCESSFUL
import com.kml.R
import com.kml.databinding.DialogChangePassBinding
import com.kml.extensions.logError
import com.kml.extensions.showToast
import com.kml.viewModels.ProfileViewModel
import com.kml.views.BaseDialog
import com.kml.views.activities.MainActivity
import io.reactivex.rxjava3.kotlin.subscribeBy

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
        viewModel.resolvePasswordChanging(newPassword, oldPassword)
            .subscribeBy(
                onSuccess = {
                    resolveResult(it)
                }, onError = { logError(it); showToast(R.string.change_pass_error) }
            )
    }

    private fun resolveResult(result: String) {
        binding.dialogOldPassword.setText("")
        binding.dialogNewPassword.setText("")
        if(result == "true") {
            showToast(R.string.pass_has_been_changed, Toast.LENGTH_LONG)
            (activity as? MainActivity)?.clearLogData()
            dismiss()
        }
        else
            showToast(result)
        binding.dialogChangePasswordProgressBar.visibility = View.GONE
    }
}