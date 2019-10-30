package com.belous.v.clrc.view.dialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.belous.v.clrc.R;
import com.belous.v.clrc.view.SettingsActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RateDialog extends DialogFragment {
    public static final String FRAGMENT_TAG = "rate_dialog";

    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;

    public RateDialog(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_rate, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.rate_button, R.id.close_button, R.id.later_button})
    void onClick(View v) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (v.getId()) {
            case R.id.rate_button:
                Activity activity = getActivity();
                if (activity != null) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
                }
            case R.id.close_button:
                editor.putBoolean(SettingsActivity.IS_VOTED, true);
                editor.apply();
                break;
            case R.id.later_button:
                editor.putLong(SettingsActivity.DAY_INSTALLATION, System.currentTimeMillis() + 259200000L);
                editor.apply();
                break;
        }
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
