package com.belous.v.clrc.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.createViewModelLazy
import com.belous.v.clrc.R
import com.belous.v.clrc.core.domain.Yeelight
import com.belous.v.clrc.ui.feature_main.MainViewModel

class RenameDialog : DialogFragment() {

    companion object {
        private const val KEY_NAME = "name"
    }

    private val viewModel by createViewModelLazy(
        viewModelClass = MainViewModel::class,
        storeProducer = { requireParentFragment().viewModelStore }
    )

    private var editText: EditText? = null
    private var yeelight: Yeelight? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val position = it.getInt(ContextDialog.YEELIGHT_IDX)
            yeelight = viewModel.loadedYeelightList.value?.get(position)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_NAME, editText!!.text.toString())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        editText = EditText(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        editText?.layoutParams = layoutParams
        if (savedInstanceState != null) {
            val name = savedInstanceState.getString(KEY_NAME, "")
            editText?.setText(name)
            editText?.setSelection(name.length)
        } else {
            editText?.setText(yeelight?.name)
            editText?.selectAll()
        }
        editText?.requestFocus()
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.rename))
            .setView(editText)
            .setNegativeButton(getString(R.string.no)) { _, _ -> dismiss() }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (editText?.text?.length ?: 0 > 0) {
                    yeelight?.copy(name = editText?.text.toString())
                        ?.let { viewModel.renameYeelight(it) }
                } else {
                    Toast.makeText(context, getString(R.string.write_name), Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .create()
    }
}