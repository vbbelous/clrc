package com.belous.v.clrc.ui.component

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.belous.v.clrc.R

class ContextDialog : DialogFragment() {

    companion object {
        const val YEELIGHT_ID = "yeelight_id"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.select)
            .setItems(
                arrayOf(getString(R.string.rename), getString(R.string.delete))
            ) { _: DialogInterface?, which: Int ->
                if (which == 0) {
                    RenameDialog().apply {
                        arguments = this@ContextDialog.arguments
                    }.show(parentFragmentManager, null)
                } else {
                    DeleteDialog().apply {
                        arguments = this@ContextDialog.arguments
                    }.show(parentFragmentManager, null)
                }
            }
            .create()
    }
}