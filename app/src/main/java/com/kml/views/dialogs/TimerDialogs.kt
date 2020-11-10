package com.kml.views.dialogs

import androidx.fragment.app.DialogFragment

abstract class TimerDialogs : DialogFragment() {

    lateinit var onAcceptListener: OnAcceptDialogListener
    lateinit var onCancelListener: OnCancelDialogListener

    fun setOnAcceptListener(operation: () -> Unit) {
        onAcceptListener = object : OnAcceptDialogListener {
            override fun onAccept() {
                operation()
            }
        }
    }

    fun setOnCancelListener(operation: () -> Unit) {
        onCancelListener = object : OnCancelDialogListener {
            override fun onCancel() {
                operation()
            }
        }
    }


}