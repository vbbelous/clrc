package com.belous.v.clrc.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.createViewModelLazy
import com.belous.v.clrc.R
import com.belous.v.clrc.ui.dialog.ContextDialog.Companion.YEELIGHT_ID
import com.belous.v.clrc.ui.feature_main.MainViewModel

class DeleteDialog : DialogFragment() {

    private val viewModel by createViewModelLazy(
        viewModelClass = MainViewModel::class,
        storeProducer = { requireParentFragment().viewModelStore }
    )

    private var yeelightId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            yeelightId = it.getInt(YEELIGHT_ID)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm_deletion))
            .setNegativeButton(getString(R.string.no), null)
            .setPositiveButton(getString(R.string.yes)) { _: DialogInterface?, _: Int ->
                yeelightId?.let {
                    viewModel.deleteYeelight(it)
                }
            }
            .create()
    }
}