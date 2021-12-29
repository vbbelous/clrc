package com.belous.v.clrc.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.belous.v.clrc.App;
import com.belous.v.clrc.R;
import com.belous.v.clrc.model.Item;
import com.belous.v.clrc.main.MainViewModel;
import com.belous.v.clrc.presenter.ListPresenter;
import com.belous.v.clrc.view.adapter.ItemsAdapter;
import com.belous.v.clrc.view.dialog.ContextDialog;
import com.belous.v.clrc.view.dialog.FoundDialog;

import java.util.List;

import javax.inject.Inject;

public class ListFragment extends BaseFragment implements ListView, SwipeRefreshLayout.OnRefreshListener {

    private FragmentActivity activity;
    private ItemsAdapter itemsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    ListPresenter listPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        setHasOptionsMenu(true);
//        Log.d("MyLogs", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        App.appComponent.inject(this);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);

        itemsAdapter = new ItemsAdapter(listPresenter);
        RecyclerView recyclerView = view.findViewById(R.id.cl_RecyclerView);
        recyclerView.setAdapter(itemsAdapter);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.d("MyLogs", "onActivityCreated");
        listPresenter.onStart(this, ViewModelProviders.of(getActivity()).get(MainViewModel.class).getShowProgress());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listPresenter.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.exit:
                activity.finish();
                return true;
            case R.id.search:
                listPresenter.onSearchClick();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void showItems(List<Item> items) {
        itemsAdapter.setData(items);
    }

    @Override
    public void showContextDialog(Item item) {
        if (getFragmentManager() != null) {
            new ContextDialog(item).show(getFragmentManager(), ContextDialog.class.getSimpleName());
        }
    }

    @Override
    public void showFoundItems(List<Item> items) {
        if (items.size() != 0) {
            if (getFragmentManager() != null) {
                new FoundDialog(items).show(getFragmentManager(), FoundDialog.class.getSimpleName());
            }
        } else {
            showErrorMsg();
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        listPresenter.onGetParamsClick();
    }

    @Override
    public void showInfoFragment(Item item) {
        Bundle bundle = new Bundle();
        bundle.putLong(InfoFragment.ITEM_ID, item.getId());

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            InfoFragment infoFragment = new InfoFragment();
            infoFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.main_layout, infoFragment, InfoView.class.getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void showErrorMsg() {
        Toast.makeText(getContext(), getString(R.string.device_not_found), Toast.LENGTH_SHORT).show();
    }
}
