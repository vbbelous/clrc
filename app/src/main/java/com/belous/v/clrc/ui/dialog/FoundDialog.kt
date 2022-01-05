package com.belous.v.clrc.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.createViewModelLazy
import com.belous.v.clrc.R
import com.belous.v.clrc.ui.feature_main.MainViewModel

class FoundDialog : DialogFragment() {

    private var editText: EditText? = null

    private val viewModel by createViewModelLazy(
        viewModelClass = MainViewModel::class,
        storeProducer = { requireParentFragment().viewModelStore }
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        editText = EditText(context)
        editText?.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        editText?.setHint(R.string.write_name)

        val resultArray =
            viewModel.foundYeelightEntityList.value?.map { yeelight -> yeelight.name }
                ?.toTypedArray()

        var selectedYeelightIdx = 0
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.select)
            .setPositiveButton(R.string.save) { _: DialogInterface?, which: Int ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    if (editText!!.text.isNotEmpty()) {
                        val yeelight =
                            viewModel.foundYeelightEntityList.value?.get(selectedYeelightIdx)
                        yeelight?.let {
                            viewModel.saveYeelight(it.copy(name = editText?.text.toString()))
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.write_name),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setView(editText)
            .setSingleChoiceItems(resultArray, 0) { _: DialogInterface?, which: Int ->
                selectedYeelightIdx = which
                editText!!.requestFocus()
            }
            .create()
    }
}