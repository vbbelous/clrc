package com.belous.v.clrc

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.belous.v.clrc.ui.dialog.ProgressDialog
import com.belous.v.clrc.ui.dialog.RateDialog
import com.belous.v.clrc.ui.feature_main.MainFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        const val IS_VOTED = "IS_VOTED"
        const val DAY_INSTALLATION = "DAY_INSTALLATION"
    }

    private var progressDialog: ProgressDialog? = null

    @Inject
    lateinit var mainStates: MainStates

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.appComponent.inject(this)

        progressDialog = ProgressDialog(this)

        val fragmentManager = supportFragmentManager
        showRateDialog(fragmentManager)

        initProgressDialog()
        initEvensObserver(window.decorView.rootView)

        if (savedInstanceState == null) {
            fragmentManager.commit {
                add<MainFragment>(R.id.main_layout)
            }
        }
    }

    private fun initProgressDialog() {
        lifecycleScope.launchWhenStarted {
            mainStates.loadingState.collectLatest { isShowing ->
                if (isShowing) {
                    progressDialog?.show()
                } else {
                    progressDialog?.dismiss()
                }
            }
        }
    }

    private fun initEvensObserver(view: View) {
        lifecycleScope.launchWhenStarted {
            mainStates.event.collectLatest { event ->
                val message = when (event) {
                    is MainStates.Event.MessageEvent -> event.message
                    is MainStates.Event.ExceptionEvent -> event.exception.message
                }
                Snackbar.make(view, message ?: getString(R.string.error), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    private fun showRateDialog(fragmentManager: FragmentManager) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (!preferences.contains(IS_VOTED)) {
            if (preferences.getLong(DAY_INSTALLATION, 0) < System.currentTimeMillis()) {
                RateDialog().show(fragmentManager, null)
            } else {
                preferences.edit {
                    putLong(DAY_INSTALLATION, System.currentTimeMillis() + 259200000L)
                }
            }
        }
    }
}