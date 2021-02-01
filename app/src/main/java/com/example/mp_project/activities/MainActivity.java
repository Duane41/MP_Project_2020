package com.example.mp_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mp_project.R;

public class MainActivity extends AppCompatActivity {
    public static final int TEXT_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToTasks(View view) {
        Intent intent = new Intent(this, TasksActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    public void goToCalendar(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    public void goToGroceries(View view) {
        Intent intent = new Intent(this, GroceriesActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    public void goToFunds(View view) {
        Intent intent = new Intent(this, FundsActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }
}
