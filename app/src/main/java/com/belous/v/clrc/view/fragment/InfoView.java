package com.belous.v.clrc.view.fragment;

import com.belous.v.clrc.R;
import com.belous.v.clrc.model.Item;

public interface InfoView {

    int POWER = R.id.power;
    int POWER_CHANGE = R.id.power_change;
    int MODE = R.id.mode;
    int MODE_CHANGE = R.id.mode_change;
    int BRIGHT = R.id.bright;
    int BRIGHT_DOWN = R.id.bright_down;
    int BRIGHT_UP = R.id.bright_up;
    int BRIGHT_1 = R.id.bright_1;
    int BRIGHT_25 = R.id.bright_25;
    int BRIGHT_50 = R.id.bright_50;
    int BRIGHT_75 = R.id.bright_75;
    int BRIGHT_100 = R.id.bright_100;
    int TEMP = R.id.temp;
    int TEMP_DOWN = R.id.temp_down;
    int TEMP_UP = R.id.temp_up;
    int SWIPE_LAYOUT = R.id.swipe_refresh_layout;

    void showItem(Item item);

    void showErrorMsg();
}
