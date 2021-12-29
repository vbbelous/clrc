package com.belous.v.clrc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.belous.v.clrc.App;
import com.belous.v.clrc.R;
import com.belous.v.clrc.model.Item;
import com.belous.v.clrc.presenter.ListPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FoundDialog extends DialogFragment {

    private static final String KEY_ITEMS = "found_items";
    private static final String KEY_NAME = "found_name";

    private List<Item> items;
    private Context context;
    private EditText editText;

    @Inject
    ListPresenter listPresenter;

    public FoundDialog() {
    }

    public FoundDialog(List<Item> items) {
        this.items = items;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        App.appComponent.inject(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_ITEMS, (ArrayList<? extends Parcelable>) items);
        outState.putString(KEY_NAME, editText.getText().toString());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        editText = new EditText(context);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        editText.setHint(R.string.write_name);

        if (savedInstanceState != null) {
            items = savedInstanceState.getParcelableArrayList(KEY_ITEMS);
            editText.setText(savedInstanceState.getString(KEY_NAME));
        }
        String[] resultArray = new String[items.size()];
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = items.get(i).getName();
        }
        int[] position = {0};

        return new AlertDialog.Builder(context)
                .setTitle(R.string.select)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    if (which == dialog.BUTTON_POSITIVE) {
                        if (editText.getText().length() > 0) {
                            Item item = items.get(position[0]);
                            item.setName(editText.getText().toString());
                            new Thread(() -> listPresenter.saveItem(item)).start();
                        } else {
                            Toast.makeText(context, context.getString(R.string.write_name), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setView(editText)
                .setSingleChoiceItems(resultArray, 0, (dialog, which) -> {
                    position[0] = which;
                    editText.requestFocus();
                })
                .create();
    }
}
