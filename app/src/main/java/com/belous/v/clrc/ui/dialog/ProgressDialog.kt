package com.belous.v.clrc.view.dialog;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.belous.v.clrc.R;

public class ProgressDialog extends AlertDialog implements LifecycleObserver {

    public ProgressDialog(Context context) {
        super(context);
        View view = View.inflate(context, R.layout.progress_dialog, null);
        this.setView(view);
        this.setCancelable(false);
    }

    @SuppressWarnings("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        dismiss();
    }
}
