package com.kml.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor


fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        val parcelFileDescriptor: ParcelFileDescriptor? = context.contentResolver.openFileDescriptor(this, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        image
    } catch (e: Exception) {
        logError(e)
        null
    }
}

fun String.toBitmap(): Bitmap? {
    return try {
        val encodeByte: ByteArray = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    } catch (e: java.lang.Exception) {
        e.message
        null
    }
}

fun Bitmap.toEncodedString(): String {
    val baos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, baos)
    val b: ByteArray = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}