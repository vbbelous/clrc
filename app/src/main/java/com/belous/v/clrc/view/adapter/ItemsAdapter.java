package com.belous.v.clrc.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.belous.v.clrc.R;
import com.belous.v.clrc.model.Item;
import com.belous.v.clrc.presenter.ListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemHolder> {

    private List<Item> itemList = new ArrayList<>();
    private ListPresenter listPresenter;

    public ItemsAdapter(ListPresenter listPresenter) {
        this.listPresenter = listPresenter;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ItemVO itemVO = ItemVO.build(itemList.get(position));
        holder.power.setImageResource(itemVO.getPowerImage());
        holder.name.setText(itemVO.getName());
        holder.mode.setImageResource(itemVO.getModeImage());
        holder.currentState.setText(itemVO.getCurrentState());
        holder.stepDown.setVisibility(itemVO.getVisibility());
        holder.currentState.setVisibility(itemVO.getVisibility());
        holder.stepUp.setVisibility(itemVO.getVisibility());
    }

    public void setData(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.power)
        ImageView power;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.mode)
        ImageView mode;

        @BindView(R.id.current_state)
        TextView currentState;

        @BindView(R.id.step_up)
        ImageView stepUp;

        @BindView(R.id.step_down)
        ImageView stepDown;

        ItemHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            power.setOnClickListener(this);
            stepUp.setOnClickListener(this);
            stepDown.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == -1) {
                listPresenter.onItemClick(itemList.get(this.getLayoutPosition()));
            } else {
                listPresenter.onSetParamsClick(view.getId(), itemList.get(this.getLayoutPosition()));
            }
        }

        @Override
        public boolean onLongClick(View view) {
            listPresenter.onItemLongClick(itemList.get(this.getLayoutPosition()));
            return true;
        }
    }
}
