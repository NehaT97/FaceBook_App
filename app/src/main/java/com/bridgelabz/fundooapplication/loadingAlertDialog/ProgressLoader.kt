package com.bridgelabz.fundooapplication.loadingAlertDialog

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import com.bridgelabz.fundooapplication.R

class ProgressLoader(private val activity: Activity) {
    private lateinit var dialog: AlertDialog


    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        var inflater: LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog, null))
        builder.setCancelable(true)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}