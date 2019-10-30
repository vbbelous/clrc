package com.belous.v.clrc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.belous.v.clrc.R;
import com.belous.v.clrc.model.Item;

public class RenameDialog extends BaseDialog {
    private static final String KEY_NAME = "name";

    private EditText editText;

    public RenameDialog() {
    }

    RenameDialog(Item item) {
        super(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_NAME, editText.getText().toString());
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        editText = new EditText(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(layoutParams);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(KEY_NAME, "");
            editText.setText(name);
            editText.setSelection(name.length());
        } else {
            editText.setText(item.getName());
            editText.selectAll();
        }

        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        return new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.rename))
                .setView(editText)
                .setNegativeButton(context.getString(R.string.no), (dialogInterface, i) -> dismiss())
                .setPositiveButton(context.getString(R.string.yes), (dialogInterface, i) -> {
                    if (editText.getText().length() > 0) {
                        item.setName(editText.getText().toString());
                        new Thread(() -> listPresenter.saveItem(item)).start();
                    } else {
                        Toast.makeText(context, context.getString(R.string.write_name), Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }
}
