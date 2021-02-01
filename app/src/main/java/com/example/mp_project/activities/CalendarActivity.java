package com.example.mp_project.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp_project.R;
import com.example.mp_project.database.DigsitViewModel;
import com.example.mp_project.database.objects.DigsitItem;
import com.example.mp_project.list_adapters.DigsitCalendarListAdapter;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView title_calendar_items_summary;
    private RecyclerView mRecyclerView;
    private DigsitCalendarListAdapter mAdapter;
    private DigsitViewModel mDigsitItemViewModel;
    private SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    private int selected_year;
    private int selected_month;
    private int selected_day;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar = Calendar.getInstance();

        mDigsitItemViewModel = ViewModelProviders.of(this).get(DigsitViewModel.class);
        title_calendar_items_summary = findViewById(R.id.uncomplete_list_title);
        title_calendar_items_summary.setText("Items for the day: ");
        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    selected_year = year;
                    selected_month = month;
                    selected_day = dayOfMonth;
                    mDigsitItemViewModel.findCalendarItems(FORMAT.format(new GregorianCalendar(year, month, dayOfMonth).getTime()));
            }
        });
        //Uncomplete items list creation
        mRecyclerView = findViewById(R.id.recyclerview_uncomplete_task);
        mAdapter = new DigsitCalendarListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //mDigsitItemViewModel.findCalendarItems(FORMAT.format(new Date(calendarView.getDate())));
        //This wil allow swipe interaction to take place between the recycler and the user
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        DigsitItem digsit_item = mAdapter.getDigsitItemAtPosition(position);
                        Toast.makeText(CalendarActivity.this, "Completing " +
                                digsit_item.getDescription(), Toast.LENGTH_LONG).show();

                        // Delete the word
                        digsit_item.setComplete(true);
                        mDigsitItemViewModel.updateDigsitItem(digsit_item);
                        mDigsitItemViewModel.findCalendarItems(FORMAT.format(new GregorianCalendar(selected_year, selected_month, selected_day).getTime()));
                    }
                });

        helper.attachToRecyclerView(mRecyclerView);

        mDigsitItemViewModel.getCalendarItems().observe(this, new Observer<List<DigsitItem>>() {
            @Override
            public void onChanged(@Nullable final List<DigsitItem> digsit_items) {
                mAdapter.setDigsitIems(digsit_items);
            }
        });
        mDigsitItemViewModel.findCalendarItems(FORMAT.format(new Date(calendarView.getDate())));
        calendar.setTime(new Date(calendarView.getDate()));
        selected_year = calendar.get(Calendar.YEAR);
        selected_month = calendar.get(Calendar.MONTH) + 1;
        selected_day = calendar.get(Calendar.DAY_OF_MONTH);
    }
}
