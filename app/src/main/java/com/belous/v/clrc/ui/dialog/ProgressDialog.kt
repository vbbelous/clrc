package com.belous.v.clrc.ui.dialog

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.belous.v.clrc.R

class ProgressDialog(context: Context) : AlertDialog(context) {

    init {
        val view = View.inflate(context, R.layout.progress_dialog, null)
        this.setView(view)
        setCancelable(false)
    }
}