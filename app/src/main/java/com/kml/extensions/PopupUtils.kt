package com.kml.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.kml.Constants.Signal.UNKNOWN_ID
import com.kml.databinding.ViewToastBinding

/////////////////////////////////////////TOAST////////////////////////////////////////////////////
@SuppressLint("ShowToast")
fun Context.showToast(
    text: CharSequence,
    duration: Int = Toast.LENGTH_SHORT,
    showCustomView: Boolean = true
): Toast =
    Toast.makeText(this, text, duration)
        .appDefault(this, UNKNOWN_ID, showCustomView, text)

@SuppressLint("ShowToast")
fun Context.showToast(
    resId: Int,
    duration: Int = Toast.LENGTH_SHORT,
    showCustomView: Boolean = true
): Toast =
    Toast.makeText(this, resId, duration)
        .appDefault(this, resId, showCustomView)

@SuppressLint("ShowToast")
fun Fragment.showToast(
    text: CharSequence,
    duration: Int = Toast.LENGTH_SHORT,
    showCustomView: Boolean = true
): Toast =
    Toast.makeText(requireContext(), text, duration)
        .appDefault(requireContext(), UNKNOWN_ID, showCustomView, text)

@SuppressLint("ShowToast")
fun Fragment.showToast(
    resId: Int,
    duration: Int = Toast.LENGTH_SHORT,
    showCustomView: Boolean = true
): Toast =
    Toast.makeText(requireContext(), resId, duration)
        .appDefault(requireContext(), resId, showCustomView)

private fun Toast.appDefault(
    context: Context,
    resId: Int, showCustomView: Boolean,
    text: CharSequence? = null
): Toast {
    val binding = ViewToastBinding.inflate(LayoutInflater.from(context))
    this.run {
        if (showCustomView) {
            binding.toastText.text = text ?: context.getString(resId)
            view = binding.root
        }
        show()
        return this
    }
}

/////////////////////////////////////////SNACK_BAR////////////////////////////////////////////////////
@SuppressLint("ShowToast")
fun Fragment.showSnackBar(
    text: CharSequence,
    duration: Int = Snackbar.LENGTH_SHORT,
    dismissOnClick: Boolean = true
) {
    if (activity != null)
        Snackbar.make(requireActivity().findViewById(android.R.id.content), text, duration)
            .appDefault(dismissOnClick)
}

@SuppressLint("ShowToast")
fun Fragment.showSnackBar(
    resId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    dismissOnClick: Boolean = true
) {
    if (activity != null)
        Snackbar.make(requireActivity().findViewById(android.R.id.content), resId, duration)
            .appDefault(dismissOnClick)
}

@SuppressLint("ShowToast")
fun AppCompatActivity.showSnackBar(
    text: CharSequence,
    duration: Int = Snackbar.LENGTH_SHORT,
    dismissOnClick: Boolean = true
) {
    Snackbar.make(findViewById(android.R.id.content), text, duration)
        .appDefault(dismissOnClick)
}

@SuppressLint("ShowToast")
fun AppCompatActivity.showSnackBar(
    resId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    dismissOnClick: Boolean = true
) {
    Snackbar.make(findViewById(android.R.id.content), resId, duration)
        .appDefault(dismissOnClick)
}

@SuppressLint("ShowToast")
fun FragmentActivity.showSnackBar(
    resId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    dismissOnClick: Boolean = true
) {
    Snackbar.make(findViewById(android.R.id.content), resId, duration)
        .appDefault(dismissOnClick)
}

fun Snackbar.appDefault(dismissOnClick: Boolean) {
    this.apply {
        if (dismissOnClick) view.setOnClickListener { dismiss() }
        show()
    }
}