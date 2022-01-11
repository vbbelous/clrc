package com.belous.v.clrc.ui.component

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.belous.v.clrc.R
import com.belous.v.clrc.databinding.DialogRateBinding
import com.belous.v.clrc.MainActivity

class RateDialog : DialogFragment(R.layout.dialog_rate) {

    private lateinit var binding: DialogRateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogRateBinding.bind(view)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        binding.rateButton.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + requireActivity().packageName)
                )
            )
            sharedPreferences.edit {
                putBoolean(MainActivity.IS_VOTED, true)
            }
            dismiss()
        }

        binding.closeButton.setOnClickListener {
            sharedPreferences.edit {
                putBoolean(MainActivity.IS_VOTED, true)
            }
            dismiss()
        }

        binding.laterButton.setOnClickListener {
            sharedPreferences.edit {
                putLong(MainActivity.DAY_INSTALLATION, System.currentTimeMillis() + 259200000L)
            }
            dismiss()
        }
    }
}