package com.belous.v.clrc;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.belous.v.clrc.other.ProgressModel;
import com.belous.v.clrc.view.SettingsActivity;
import com.belous.v.clrc.view.dialog.ProgressDialog;
import com.belous.v.clrc.view.dialog.RateDialog;
import com.belous.v.clrc.view.fragment.ListFragment;
import com.belous.v.clrc.view.fragment.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        showRateDialog(fragmentManager);
        initProgressDialog();

        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.main_layout, new ListFragment(), ListView.class.getSimpleName())
                    .commit();
        }
    }

    private void showRateDialog(FragmentManager fragmentManager) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sharedPreferences.contains(SettingsActivity.IS_VOTED)) {
            if (sharedPreferences.contains(SettingsActivity.DAY_INSTALLATION)) {
                if (sharedPreferences.getLong(SettingsActivity.DAY_INSTALLATION, 0) < System.currentTimeMillis()) {
                    new RateDialog(sharedPreferences).show(fragmentManager, RateDialog.FRAGMENT_TAG);
                }
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(SettingsActivity.DAY_INSTALLATION, System.currentTimeMillis() + 259200000L);
                editor.apply();
            }
        }
    }

    private void initProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        getLifecycle().addObserver(progressDialog);
        ViewModelProviders.of(this).get(ProgressModel.class).getShowProgress().observe(this, aBoolean -> {
            if (aBoolean) {
                progressDialog.show();
            } else {
                progressDialog.dismiss();
            }
        });
    }
}
