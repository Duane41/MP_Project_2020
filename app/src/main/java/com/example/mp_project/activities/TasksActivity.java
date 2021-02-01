package com.example.mp_project.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp_project.R;
import com.example.mp_project.database.objects.DigsitItem;
import com.example.mp_project.database.DigsitViewModel;
import com.example.mp_project.list_adapters.DigsitCompleteTaskListAdapter;
import com.example.mp_project.list_adapters.DigsitUncompleteListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TasksActivity extends AppCompatActivity {


    private ArrayList<String> items = new ArrayList<String>();
    private Set<String> items_set;
    private ArrayAdapter<String> adapter;
    private String ITEMS_KEY = "task_items";

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.hellosharedprefs";
    private ListView itemList;

    private String pop_up_input = "";

    private static final int NEW_DIGSIT_ITEM = 1;

    private DigsitViewModel mDigsitItemViewModel;

    private RecyclerView mRecyclerView_uncomplete;
    private DigsitUncompleteListAdapter mAdapter_uncomplete;

    private RecyclerView mRecyclerView_complete;
    private DigsitCompleteTaskListAdapter mAdapter_complete;

    private TextView text_view_uncomplete;
    private TextView text_view_complete;
    public static final String CREATE_ITEM_ACTIVITY= "CREATE_ITEM_ACTIVITY";
    public static final String ACTIVITY_NAME = "CREATE_TASK_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        text_view_uncomplete = findViewById(R.id.uncomplete_list_title);
        text_view_complete = findViewById(R.id.complete_list_title);
        text_view_uncomplete.setText("Uncomplete tasks");
        text_view_complete.setText("Complete tasks");
        /*itemList = findViewById(R.id.task_list);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        items_set = new HashSet<String>(mPreferences.getStringSet(ITEMS_KEY, new HashSet<String>()));
        items.addAll(items_set);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        itemList.setAdapter(adapter);

        final AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(TasksActivity.this);
        myAlertBuilder.setTitle("Add Task");

        //This will set the input field
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        myAlertBuilder.setView(input);

        myAlertBuilder.setPositiveButton("OK", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                        pop_up_input = input.getText().toString();
                        items.add(pop_up_input);
                        preferencesEditor.putStringSet(ITEMS_KEY, new HashSet<String>(items)).apply();

                        Toast.makeText(getApplicationContext(), "Saved!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        myAlertBuilder.setNegativeButton("Cancel", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        */

        //Uncomplete task list creation
        mRecyclerView_uncomplete = findViewById(R.id.recyclerview_uncomplete_task);
        mAdapter_uncomplete = new DigsitUncompleteListAdapter(this);
        mRecyclerView_uncomplete.setAdapter(mAdapter_uncomplete);
        mRecyclerView_uncomplete.setLayoutManager(new LinearLayoutManager(this));


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
                        DigsitItem digsit_item = mAdapter_uncomplete.getDigsitItemAtPosition(position);
                        Toast.makeText(TasksActivity.this, "Completing " +
                                digsit_item.getDescription(), Toast.LENGTH_LONG).show();

                        // Delete the word
                            digsit_item.setComplete(true);
                            mDigsitItemViewModel.updateDigsitItem(digsit_item);
                    }
                });

        helper.attachToRecyclerView(mRecyclerView_uncomplete);


        mDigsitItemViewModel = ViewModelProviders.of(this).get(DigsitViewModel.class);
        mDigsitItemViewModel.getAllUncompleteTaskItems().observe(this, new Observer<List<DigsitItem>>() {
            @Override
            public void onChanged(@Nullable final List<DigsitItem> digsit_items) {
                // Update the cached copy of the words in the adapter.
                mAdapter_uncomplete.setDigsitIems(digsit_items);
            }
        });

        //Complete task list creation
        mRecyclerView_complete = findViewById(R.id.recyclerview_complete_task);
        mAdapter_complete = new DigsitCompleteTaskListAdapter(this);
        mRecyclerView_complete.setAdapter(mAdapter_complete);
        mRecyclerView_complete.setLayoutManager(new LinearLayoutManager(this));


        //This wil allow swipe interaction to take place between the recycler and the user
        ItemTouchHelper helper_complete = new ItemTouchHelper(
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
                        DigsitItem digsit_item = mAdapter_complete.getDigsitItemAtPosition(position);
                        Toast.makeText(TasksActivity.this, "Deleting " +
                                digsit_item.getDescription(), Toast.LENGTH_LONG).show();

                        // Delete the word
                        mDigsitItemViewModel.deleteDigsitItem(digsit_item);
                    }
                });

        helper_complete.attachToRecyclerView(mRecyclerView_complete);

        mDigsitItemViewModel.getAllCompleteTaskItems().observe(this, new Observer<List<DigsitItem>>() {
            @Override
            public void onChanged(@Nullable final List<DigsitItem> digsit_items) {
                // Update the cached copy of the words in the adapter.
                mAdapter_complete.setDigsitIems(digsit_items);
            }
        });


        FloatingActionButton fab = findViewById(R.id.add_task_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TasksActivity.this, CreateDigsitItemActivity.class);
                intent.putExtra(CREATE_ITEM_ACTIVITY, ACTIVITY_NAME);
                startActivityForResult(intent, NEW_DIGSIT_ITEM);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String from_activity = "";
        if (data != null) {
            from_activity = data.getStringExtra(CreateDigsitItemActivity.CREATE_ITEM_ACTIVITY);
        }
        //Creates a task
        if (resultCode == RESULT_OK && from_activity.equalsIgnoreCase(ACTIVITY_NAME)) {
            DigsitItem digsit_item = new DigsitItem(data.getStringExtra(CreateDigsitItemActivity.CREATE_DESCRIPTION), data.getStringExtra(CreateDigsitItemActivity.CREATE_DUE_DATE), true);
            mDigsitItemViewModel.insertDigsitItem(digsit_item);
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    "Task creation cancelled.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
