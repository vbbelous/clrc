package com.belous.v.clrc.view.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.belous.v.clrc.model.Item;
import com.belous.v.clrc.model.db.ItemDao;
import com.belous.v.clrc.other.di.App;
import com.belous.v.clrc.presenter.ListPresenter;

import javax.inject.Inject;

public abstract class BaseDialog extends DialogFragment {

    private static final String KEY_ITEM = "item";

    protected Context context;
    protected Item item;

    @Inject
    ListPresenter listPresenter;

    public BaseDialog() {
    }

    BaseDialog(Item item) {
        this.item = item;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_ITEM, item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = getContext();
        App.getAppComponent().inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            item = savedInstanceState.getParcelable(KEY_ITEM);
        }
    }
}
