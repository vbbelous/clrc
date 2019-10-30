package com.belous.v.clrc.presenter;

import androidx.lifecycle.MutableLiveData;

import com.belous.v.clrc.view.fragment.InfoView;

public interface InfoPresenter {

    void onStart(InfoView infoView, long itemId, MutableLiveData<Boolean> progressBar);

    void onDestroy();

    void onGetParamsClick();

    void onSetParamsClick(int viewId);
}
