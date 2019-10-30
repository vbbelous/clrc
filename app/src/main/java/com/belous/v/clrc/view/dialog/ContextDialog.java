package com.belous.v.clrc.view.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.belous.v.clrc.R;
import com.belous.v.clrc.model.Item;

public class ContextDialog extends BaseDialog {

    public ContextDialog() {
    }

    public ContextDialog(Item item) {
        super(item);
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.select)
                .setItems(new String[]{getString(R.string.rename), getString(R.string.delete)}, (dialog, which) -> {
                    if (which == 0) {
                        new RenameDialog(item).show(getFragmentManager(), null);
                    } else {
                        new DeleteDialog(item).show(getFragmentManager(), null);

                    }
                })
                .create();
    }
}
