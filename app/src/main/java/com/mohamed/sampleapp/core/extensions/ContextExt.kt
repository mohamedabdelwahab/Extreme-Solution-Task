package com.mohamed.sampleapp.core.extensions

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.mohamed.sampleapp.R

fun Context.showToast(message: String, connectionError: Boolean = false) {
    if (connectionError) {
        showErrorMessage(getString(R.string.check_internet_connections))
    } else {
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        showErrorMessage(message)

    }
    Log.d("error", message)
}

fun Context.showGenericAlertDialog(code: Int = 0, message: String = "") {
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
    }.show()

    if (code == 403 || code == 401) {
        //startActivity(Intent(this, ActivityAuthorization::class.java))
        (this as Activity).finish()
    }
}

fun Context.showSuccessMessage(message: String = "", onClickOk: (() -> Unit)? = null) {
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            if (onClickOk != null) {
                onClickOk()
            }
            dialog.dismiss()
        }
    }.show()
}

fun Context.showErrorMessage(message: String = "") {
    AlertDialog.Builder(this).apply {
        setCancelable(false)
        setMessage(message)
        setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}

fun Context.showInfoMessage(message: String = "") {
    //Toasty.info(this, message, Toast.LENGTH_SHORT, true).show()
}