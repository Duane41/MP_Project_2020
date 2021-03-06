package com.example.mp_project.list_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mp_project.R;
import com.example.mp_project.database.objects.DigsitItem;

import java.util.List;

public class DigsitCalendarListAdapter extends RecyclerView.Adapter<DigsitCalendarListAdapter.CalendarViewHolder> {

    private final LayoutInflater mInflater;
    private List<DigsitItem> digsit_items; // Cached copy of calendar items

    public DigsitCalendarListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new CalendarViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        if (digsit_items != null) {
            DigsitItem current = digsit_items.get(position);
            holder.digsit_description_view.setText(current.getDescription());
            holder.digsit_time_remaining_view.setText(current.getDueDate());
        } else {
            // Covers the case of data not being ready yet.
            holder.digsit_description_view.setText("No item to display");
        }
    }

    public void setDigsitIems(List<DigsitItem> items){
        digsit_items = items;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    @Override
    public int getItemCount() {
        if (digsit_items != null)
            return digsit_items.size();
        else return 0;
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {
        public final TextView digsit_description_view;
        public final TextView digsit_time_remaining_view;
        final DigsitCalendarListAdapter mAdapter;

        private CalendarViewHolder(View itemView, DigsitCalendarListAdapter adapter) {
            super(itemView);
            digsit_description_view = itemView.findViewById(R.id.recycler_view_column_1);
            digsit_time_remaining_view = itemView.findViewById(R.id.recycler_view_column_2);
            this.mAdapter = adapter;
        }
    }

    public DigsitItem getDigsitItemAtPosition (int position) {
        return digsit_items.get(position);
    }
}