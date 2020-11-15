package com.kml.data.app

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kml.data.listeners.OnAcceptDialogListener
import com.kml.data.listeners.OnCancelDialogListener

abstract class AppDialogs : DialogFragment() {

    lateinit var onAcceptListener: OnAcceptDialogListener
    lateinit var onCancelListener: OnCancelDialogListener
    var lastTimeClicked: Long = 0

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}