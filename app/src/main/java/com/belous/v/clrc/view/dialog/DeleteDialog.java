package com.belous.v.clrc.view.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.belous.v.clrc.R;
import com.belous.v.clrc.model.Item;

public class DeleteDialog extends BaseDialog {


    public DeleteDialog() {
    }

    DeleteDialog(Item item) {
        super(item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.confirm_deletion))
                .setNegativeButton(context.getString(R.string.no), null)
                .setPositiveButton(context.getString(R.string.yes), (dialogInterface, i) -> new Thread(() -> listPresenter.saveItem(item)).start())
                .create();
    }
}
