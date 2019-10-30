package com.belous.v.clrc.view.adapter;

import android.view.View;

import com.belous.v.clrc.R;
import com.belous.v.clrc.model.Item;

public class ItemVO {

    private String name;
    private String currentState;
    private int powerImage;
    private int visibility;
    private int modeImage;
    private boolean online;

    private ItemVO(String name, String currentState, int powerImage, int visibility, int modeImage, boolean online) {
        this.name = name;
        this.currentState = currentState;
        this.powerImage = powerImage;
        this.visibility = visibility;
        this.modeImage = modeImage;
        this.online = online;
    }

    String getName() {
        return name;
    }

    String getCurrentState() {
        return currentState;
    }

    int getPowerImage() {
        return powerImage;
    }

    int getVisibility() {
        return visibility;
    }

    int getModeImage() {
        return modeImage;
    }

    boolean isOnline() {
        return online;
    }

    public static ItemVO build(Item item) {
        return new ItemVO(item.getName(),
                String.valueOf(item.getBright()),
                item.isOnline() ? item.isPower() ? R.mipmap.power_on : R.mipmap.power_off : R.mipmap.offline,
                item.isPower() && item.isOnline() ? View.VISIBLE : View.INVISIBLE,
                item.isActive_mode() ? R.mipmap.moon : R.mipmap.sun,
                item.isOnline());
    }
}
