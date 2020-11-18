package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.data.externalDbOperations.DbChangePass
import com.kml.viewModels.ProfileViewModel
import kotlinx.android.synthetic.main.dialog_change_pass.view.*

class ChangePassDialog(private val viewModel: ProfileViewModel) : AppDialogs() {

    lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        root = layoutInflater.inflate(R.layout.dialog_change_pass, null)

        builder.setView(root)

        root.apply {
            btn_dialog_change_pass_accept.setOnClickListener {
                changePass(dialog_old_password.text.toString(), dialog_new_password.text.toString())
            }
            btn_dialog_change_pass_cancel.setOnClickListener { dismiss() }
        }

        return builder.create()
    }

    private fun changePass(oldPassword: String, newPassword: String) {

        val validationResult = viewModel.validatePassword(oldPassword, newPassword)
        if (validationResult != ProfileViewModel.VALIDATION_SUCCESSFUL) {
            Toast.makeText(requireContext(), validationResult, Toast.LENGTH_SHORT).show()
            return
        }

        root.dialog_change_password_progress_bar.visibility = View.VISIBLE
        val changeOperation = viewModel.resolvePasswordChanging(newPassword, oldPassword)

        changeOperation.setOnResultListener {
            resolveResult(it)
        }
    }

    private fun resolveResult(result: String) {
        if (result == DbChangePass.CHANGE_SUCCESSFUL) {
            dismiss()
        }
        root.dialog_old_password.setText("")
        root.dialog_new_password.setText("")
        Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
        root.dialog_change_password_progress_bar.visibility = View.GONE
    }
}