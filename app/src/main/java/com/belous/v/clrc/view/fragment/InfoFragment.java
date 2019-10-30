package com.belous.v.clrc.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.belous.v.clrc.R;
import com.belous.v.clrc.model.Item;
import com.belous.v.clrc.other.ProgressModel;
import com.belous.v.clrc.other.di.App;
import com.belous.v.clrc.presenter.InfoPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoFragment extends BaseFragment implements InfoView, SwipeRefreshLayout.OnRefreshListener {

    static final String ITEM_ID = "item_id";

    @BindView(BRIGHT)
    TextView bright;

    @BindView(MODE)
    TextView mode;

    @BindView(TEMP)
    TextView temp;

    @BindView(POWER_CHANGE)
    TextView powerChange;

    @BindView(MODE_CHANGE)
    TextView modeChange;

    @BindView(SWIPE_LAYOUT)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    InfoPresenter infoPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, container, false);
        App.getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            long itemId = bundle.getLong(ITEM_ID);
            infoPresenter.onStart(this, itemId, ViewModelProviders.of(getActivity()).get(ProgressModel.class).getShowProgress());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        infoPresenter.onDestroy();
    }

    @Override
    public void showItem(Item item) {
        powerChange.setTextColor(item.isPower() && item.isOnline() ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
        modeChange.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(item.isActive_mode() ? R.mipmap.status_on : R.mipmap.status_off));
        String brightText = item.isPower() ? item.getBright() + "%" : getString(R.string.off);
        if (!item.isOnline()) {
            brightText = getString(R.string.offline);
        }
        bright.setText(brightText);
        mode.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(item.isActive_mode() ? R.mipmap.moon : R.mipmap.sun), null);
        String tempText = getString(R.string.cl_temp) + " " + (item.getCt().isEmpty() ? 3400 : item.getCt()) + "K";
        temp.setText(tempText);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        infoPresenter.onGetParamsClick();
    }

    @OnClick({MODE_CHANGE, POWER_CHANGE, BRIGHT_UP, BRIGHT_DOWN, BRIGHT_1, BRIGHT_25, BRIGHT_50, BRIGHT_75, BRIGHT_100, TEMP_UP, TEMP_DOWN})
    void onClick(View view) {
        infoPresenter.onSetParamsClick(view.getId());
    }

    @Override
    public void showErrorMsg() {
        Toast.makeText(getContext(), getString(R.string.device_not_found), Toast.LENGTH_SHORT).show();
    }
}
