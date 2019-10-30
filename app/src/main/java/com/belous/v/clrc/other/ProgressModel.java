package com.belous.v.clrc.other;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProgressModel extends ViewModel {

    private MutableLiveData<Boolean> showProgress;

    public MutableLiveData<Boolean> getShowProgress() {
        if (showProgress == null) {
            showProgress = new MutableLiveData<>();
        }
        return showProgress;
    }
}
