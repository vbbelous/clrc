package com.belous.v.clrc.presenter;

import androidx.lifecycle.MutableLiveData;

import com.belous.v.clrc.R;
import com.belous.v.clrc.model.Item;
import com.belous.v.clrc.model.db.ItemDao;
import com.belous.v.clrc.model.net.Yeelight;
import com.belous.v.clrc.view.fragment.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListPresenterImpl implements ListPresenter {

    private Disposable disposable;
    private Disposable actionDisposable;

    private ItemDao itemDao;

    private ListView listView;
    private MutableLiveData<Boolean> progressBar;

    private List<Item> itemList;

    public ListPresenterImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public void onStart(ListView listView, MutableLiveData<Boolean> progressBar) {
        this.listView = listView;
        this.progressBar = progressBar;

        if (disposable == null || disposable.isDisposed()) {
            disposable = itemDao.getAll()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(items -> itemList = items)
                    .subscribe(listView::showItems);
        }
        if (actionDisposable != null && !actionDisposable.isDisposed()) {
            showProgressBar();
        }
    }

    @Override
    public void onDestroy() {
        hideProgressBar();
        disposable.dispose();
        listView = null;
        progressBar = null;
    }

    @Override
    public void onSearchClick() {
        showProgressBar();
        Set<Item> items = new HashSet<>();
        actionDisposable = Observable.fromCallable(Yeelight::searchDevices)
                .subscribeOn(Schedulers.io())
                .doFinally(this::hideProgressBar)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable::fromIterable)
                .map(Item::build)
                .subscribe(items::add,
                        errorMsg -> listView.showErrorMsg(),
                        () -> listView.showFoundItems(new ArrayList<>(items)));
    }


//    @Override
//    public void onSearchClick() {
//        List<Item> items = new ArrayList<>();
//        String one = "color_mode=2, sat=0, bright=20, rgb=0, ct=4000, name=, hue=0, model=ceiling2, id=0x0000000003b5a3f4, power=off, fw_ver=34, support=get_prop set_default set_power toggle set_bright set_scene cron_add cron_get cron_del start_cf stop_cf set_ct_abx set_name set_adjust adjust_bright adjust_ct, Location=yeelight://192.168.30.101:55443";
//        String two = "color_mode=2, sat=0, bright=20, rgb=0, ct=4389, name=, hue=0, model=ct_bulb, id=0x0000000005e7788b, power=off, fw_ver=35, support=get_prop set_default set_power toggle set_bright start_cf stop_cf set_scene cron_add cron_get cron_del set_ct_abx set_adjust adjust_bright adjust_ct set_music set_name, Location=yeelight://192.168.30.104:55443";
//        List<String> str = new ArrayList<>();
//        str.add(one);
//        str.add(two);
//        for (String i : str) {
//            Map<String, String> params = new HashMap<>();
//            for (String j : i.split(", ")) {
//                String[] k = j.split("=");
//                if (k.length > 1) {
//                    params.put(k[0], k[1]);
//                } else {
//                    params.put(k[0], "");
//                }
//            }
//            items.add(Item.build(params));
//        }
//        listView.showFoundItems(items);
//    }

    @Override
    public void onGetParamsClick() {
        showProgressBar();
        actionDisposable = Observable.fromIterable(itemList)
                .subscribeOn(Schedulers.io())
                .doFinally(() -> {
                    hideProgressBar();
                    itemDao.update(itemList);
                })
                .subscribe(item -> item.setParams(Yeelight.getParams(item.getIp(), item.getPort())));
    }

    @Override
    public void onSetParamsClick(int viewId, Item item) {
        Queue<String> args = new LinkedList<>();
        switch (viewId) {
            case R.id.power:
                args.addAll(Arrays.asList(Yeelight.SET_POWER, (item.isPower() ? "\"off\"" : "\"on\""), "\"smooth\"", "500"));
                break;
            case R.id.step_down:
                int downBright = item.getBright() - 25;
                downBright = downBright <= 0 ? 1 : downBright;
                args.addAll(Arrays.asList(Yeelight.SET_BRIGHT, String.valueOf(downBright), "\"smooth\"", "500"));
                break;
            case R.id.step_up:
                int upBright = item.getBright() + 25;
                upBright = upBright > 100 ? 100 : upBright;
                args.addAll(Arrays.asList(Yeelight.SET_BRIGHT, String.valueOf(upBright), "\"smooth\"", "500"));
                break;
        }

        showProgressBar();
        actionDisposable = Single.fromCallable(() -> Yeelight.setParams(item.getIp(), item.getPort(), args))
                .subscribeOn(Schedulers.io())
                .doFinally(this::hideProgressBar)
                .subscribe((params) -> {
                    item.setParams(params);
                    itemDao.update(item);
                });
    }

    @Override
    public void onItemClick(Item item) {
        listView.showInfoFragment(item);
    }

    @Override
    public void onItemLongClick(Item item) {
        listView.showContextDialog(item);
    }

    public void saveItem(Item item) {
        new Thread(() -> itemDao.insert(item)).start();
    }

    public void deleteItem(Item item) {
        new Thread(() -> itemDao.delete(item)).start();
    }

    private void showProgressBar() {
        progressBar.postValue(true);
    }

    private void hideProgressBar() {
        progressBar.postValue(false);
    }
}
