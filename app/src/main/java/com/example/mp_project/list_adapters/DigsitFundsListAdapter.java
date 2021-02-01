package com.example.mp_project.list_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp_project.R;
import com.example.mp_project.activities.FundsActivity;
import com.example.mp_project.database.DigsitViewModel;
import com.example.mp_project.database.objects.DigsitItem;
import com.example.mp_project.database.objects.Funds;
import com.example.mp_project.database.objects.User;

import java.text.DecimalFormat;
import java.util.List;

public class DigsitFundsListAdapter extends RecyclerView.Adapter<DigsitFundsListAdapter.FundsViewHolder> {

    private final LayoutInflater mInflater;
    private List<Funds> funds; // Cached copy of calendar items
    private DecimalFormat df = new DecimalFormat("#.##");
    private DigsitViewModel mDigsitItemViewModel;
    private OnItemListener onItemListener;
    public DigsitFundsListAdapter(Context context, DigsitViewModel in_model, OnItemListener onItemListener) {
        mDigsitItemViewModel = in_model;
        mInflater = LayoutInflater.from(context);
        this.onItemListener = onItemListener;
    }


    @Override
    public FundsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new FundsViewHolder(itemView, this, onItemListener);
    }

    @Override
    public void onBindViewHolder(FundsViewHolder holder, int position) {
        if (funds != null) {
            Funds current = funds.get(position);
            holder.digsit_description_view.setText(current.getName());
            if (current.getAmount() < 0) {
                holder.digsit_time_remaining_view.setText("R" + df.format(current.getAmount()  * -1) + " to");
            } else if(current.getAmount() > 0) {
                holder.digsit_time_remaining_view.setText("R" + df.format(current.getAmount()) + " from");
            } else {
                holder.digsit_time_remaining_view.setText("Settled up!");
            }

        } else {
            // Covers the case of data not being ready yet.
            holder.digsit_description_view.setText("No item to display");
        }
    }

    public void setDigsitIems(List<Funds> items) {
        funds = items;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    @Override
    public int getItemCount() {
        if (funds != null)
            return funds.size();
        else return 0;
    }

    class FundsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView digsit_description_view;
        public final TextView digsit_time_remaining_view;
        final DigsitFundsListAdapter mAdapter;
        OnItemListener on_item_listen;
        private FundsViewHolder(View itemView, DigsitFundsListAdapter adapter, OnItemListener on_item_listen) {
            super(itemView);
            digsit_description_view = itemView.findViewById(R.id.recycler_view_column_1);
            digsit_time_remaining_view = itemView.findViewById(R.id.recycler_view_column_2);
            this.mAdapter = adapter;
            this.on_item_listen = on_item_listen;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            on_item_listen.OnItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener {
        void OnItemClick(int position);
    }

    public Funds getDigsitItemAtPosition (int position) {
        return funds.get(position);
    }
}