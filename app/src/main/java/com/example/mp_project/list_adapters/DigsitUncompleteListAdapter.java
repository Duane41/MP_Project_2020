package com.example.mp_project.list_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mp_project.R;
import com.example.mp_project.database.objects.DigsitItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DigsitUncompleteListAdapter extends RecyclerView.Adapter<DigsitUncompleteListAdapter.UncompleteTaskViewHolder> {

    private final LayoutInflater mInflater;
    private List<DigsitItem> digsit_items; // Cached copy of words
    private SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    private Calendar calendar;
    public DigsitUncompleteListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public UncompleteTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        calendar = Calendar.getInstance();
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new UncompleteTaskViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(UncompleteTaskViewHolder holder, int position) {
        if (digsit_items != null) {
            DigsitItem current = digsit_items.get(position);
            holder.digsit_description_view.setText(current.getDescription());
            try {
                Long to_due = FORMAT.parse(current.getDueDate()).getTime() - calendar.getTimeInMillis();
                if (calendar.getTimeInMillis() + to_due > calendar.getTimeInMillis() && TimeUnit.MILLISECONDS.toDays(to_due) == 0) {
                    holder.digsit_time_remaining_view.setText( "Tomorrow");
                } else if (TimeUnit.MILLISECONDS.toDays(to_due) == 0) {
                    holder.digsit_time_remaining_view.setText("Today!");
                } else if (to_due < 0) {
                    holder.digsit_time_remaining_view.setText("Expired!");
                } else if (TimeUnit.MILLISECONDS.toDays(to_due) + 1 > 1) {
                    holder.digsit_time_remaining_view.setText(TimeUnit.MILLISECONDS.toDays(to_due ) + 1 + " days left");
                }  else if (TimeUnit.MILLISECONDS.toDays(to_due) + 1 == 1) {
                    holder.digsit_time_remaining_view.setText(TimeUnit.MILLISECONDS.toDays(to_due) + " day left");
                }
            } catch (ParseException ex) {
                Log.e("Exception", ex.getLocalizedMessage());
            }
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

    class UncompleteTaskViewHolder extends RecyclerView.ViewHolder {
        public final TextView digsit_description_view;
        public final TextView digsit_time_remaining_view;
        final DigsitUncompleteListAdapter mAdapter;

        private UncompleteTaskViewHolder(View itemView, DigsitUncompleteListAdapter adapter) {
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