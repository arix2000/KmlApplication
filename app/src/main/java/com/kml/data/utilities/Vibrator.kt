package com.kml.data.utilities

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class Vibrator(context: Context) {

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun longVibrate() {
        if (Build.VERSION.SDK_INT >= 26)
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        else
            vibrator.vibrate(200)

    }

    fun shortVibrate() {
        if (Build.VERSION.SDK_INT >= 26)
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        else
            vibrator.vibrate(100)
    }

    fun cancel() {
        vibrator.cancel()
    }

}