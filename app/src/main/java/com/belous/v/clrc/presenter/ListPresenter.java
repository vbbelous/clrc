package com.belous.v.clrc.presenter;

import androidx.lifecycle.MutableLiveData;

import com.belous.v.clrc.model.Item;
import com.belous.v.clrc.view.fragment.ListView;

public interface ListPresenter {

    void onStart(ListView listView, MutableLiveData<Boolean> progressBar);

    void onDestroy();

    void onSearchClick();

    void onGetParamsClick();

    void onSetParamsClick(int viewId, Item item);

    void onItemClick(Item item);

    void onItemLongClick(Item item);

    void saveItem(Item item);

    void deleteItem(Item item);
}
