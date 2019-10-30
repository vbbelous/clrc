package com.belous.v.clrc.view.fragment;

import com.belous.v.clrc.model.Item;

import java.util.List;

public interface ListView {

    void showItems(List<Item> items);

    void showFoundItems(List<Item> item);

    void showContextDialog(Item item);

    void showInfoFragment(Item item);

    void showErrorMsg();
}
