package com.belous.v.clrc.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import com.belous.v.clrc.R
import com.belous.v.clrc.view.dialog.ProgressDialog
import com.belous.v.clrc.view.dialog.RateDialog
import com.belous.v.clrc.view.fragment.ListFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        const val IS_VOTED = "IS_VOTED"
        const val DAY_INSTALLATION = "DAY_INSTALLATION"
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentManager = supportFragmentManager
        showRateDialog(fragmentManager)
        initProgressDialog()

        if (savedInstanceState == null) {
            fragmentManager.commit {
                add<ListFragment>(R.id.main_layout)
            }
        }
    }

    private fun showRateDialog(fragmentManager: FragmentManager) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (!preferences.contains(IS_VOTED)) {
            if (preferences.getLong(DAY_INSTALLATION, 0) < System.currentTimeMillis()) {
                RateDialog(preferences).show(fragmentManager, RateDialog.FRAGMENT_TAG)
            } else {
                preferences.edit {
                    putLong(DAY_INSTALLATION, System.currentTimeMillis() + 259200000L)
                }
            }
        }
    }

    private fun initProgressDialog() {
        val progressDialog = ProgressDialog(this)

        viewModel.showProgress.observe(this) { isShowing ->
            if (isShowing) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        }
    }
}