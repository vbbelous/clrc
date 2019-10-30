package com.belous.v.clrc.presenter;

import androidx.lifecycle.MutableLiveData;

import com.belous.v.clrc.model.Item;
import com.belous.v.clrc.model.db.ItemDao;
import com.belous.v.clrc.model.net.Yeelight;
import com.belous.v.clrc.view.fragment.InfoView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InfoPresenterImpl implements InfoPresenter {

    private ItemDao itemDao;
    private InfoView infoView;
    private Item item;
    private MutableLiveData<Boolean> progressBar;

    private Disposable disposable;
    private Disposable actionDisposable;

    public InfoPresenterImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public void onStart(InfoView infoView, long itemId, MutableLiveData<Boolean> progressBar) {
        this.infoView = infoView;
        this.progressBar = progressBar;
        if (disposable == null || disposable.isDisposed()) {
            disposable = itemDao.get(itemId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(i -> {
                        this.item = i;
                        infoView.showItem(i);
                    });
        }

        if (actionDisposable != null && !actionDisposable.isDisposed()) {
            showProgressBar();
        }
    }

    @Override
    public void onDestroy() {
        hideProgressBar();
        disposable.dispose();
        infoView = null;
        progressBar = null;
    }

    @Override
    public void onGetParamsClick() {
        showProgressBar();
        actionDisposable = Single.fromCallable(() -> Yeelight.getParams(item.getIp(), item.getPort()))
                .subscribeOn(Schedulers.io())
                .doFinally(this::hideProgressBar)
                .subscribe(params -> {
                    item.setParams(params);
                    itemDao.update(item);
                }, throwable -> infoView.showErrorMsg());
    }

    @Override
    public void onSetParamsClick(int viewId) {
        Queue<String> args = new LinkedList<>();
        switch (viewId) {
            case InfoView.POWER_CHANGE:
                args.addAll(Arrays.asList(Yeelight.SET_POWER, (item.isPower() ? "\"off\"" : "\"on\""), "\"smooth\"", "500"));
                break;
            case InfoView.MODE_CHANGE:
                args.addAll(Arrays.asList(Yeelight.SET_POWER, (item.isPower() ? "\"on\"" : "\"off\""), "\"smooth\"", "500", (item.isActive_mode() ? "1" : "5")));
                break;
            case InfoView.BRIGHT_DOWN:
                int downBright = item.getBright() - 10;
                downBright = downBright <= 0 ? 1 : downBright;
                args.addAll(Arrays.asList(Yeelight.SET_BRIGHT, String.valueOf(downBright), "\"smooth\"", "500"));
                break;
            case InfoView.BRIGHT_UP:
                int upBright = item.getBright() + 10;
                upBright = upBright > 100 ? 100 : upBright;
                args.addAll(Arrays.asList(Yeelight.SET_BRIGHT, String.valueOf(upBright), "\"smooth\"", "500"));
                break;
            case InfoView.BRIGHT_1:
                args.addAll(Arrays.asList(Yeelight.SET_BRIGHT, "1", "\"smooth\"", "500"));
                break;
            case InfoView.BRIGHT_25:
                args.addAll(Arrays.asList(Yeelight.SET_BRIGHT, "25", "\"smooth\"", "500"));
                break;
            case InfoView.BRIGHT_50:
                args.addAll(Arrays.asList(Yeelight.SET_BRIGHT, "50", "\"smooth\"", "500"));
                break;
            case InfoView.BRIGHT_75:
                args.addAll(Arrays.asList(Yeelight.SET_BRIGHT, "75", "\"smooth\"", "500"));
                break;
            case InfoView.BRIGHT_100:
                args.addAll(Arrays.asList(Yeelight.SET_BRIGHT, "100", "\"smooth\"", "500"));
                break;
            case InfoView.TEMP_DOWN:
                int downTemp = Integer.parseInt(item.getCt()) - 500;
                downTemp = downTemp <= 2700 ? 2700 : downTemp;
                args.addAll(Arrays.asList(Yeelight.SET_CT_ABX, String.valueOf(downTemp), "\"smooth\"", "500"));
                break;
            case InfoView.TEMP_UP:
                int upTemp = Integer.parseInt(item.getCt()) + 500;
                upTemp = upTemp > 6500 ? 6500 : upTemp;
                args.addAll(Arrays.asList(Yeelight.SET_CT_ABX, String.valueOf(upTemp), "\"smooth\"", "500"));
                break;
        }

        showProgressBar();
        actionDisposable = Single.fromCallable(() -> Yeelight.setParams(item.getIp(), item.getPort(), args))
                .subscribeOn(Schedulers.io())
                .doFinally(this::hideProgressBar)
                .subscribe(params -> {
                    item.setParams(params);
                    itemDao.update(item);
                }, throwable -> infoView.showErrorMsg());
    }

    private void showProgressBar() {
        progressBar.postValue(true);
    }

    private void hideProgressBar() {
        progressBar.postValue(false);
    }
}
