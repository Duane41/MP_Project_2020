package com.example.mp_project.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mp_project.R;
import com.example.mp_project.database.objects.Funds;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateDigsitItemActivity extends AppCompatActivity {
    public static final String CREATE_DESCRIPTION = "CREATE_DESCRIPTION";
    public static final String CREATE_DUE_DATE = "CREATE_DUE_DATE";
    public static final String CREATE_AMOUNT = "CREATE_AMOUNT";
    public static final String CREATE_ITEM_ACTIVITY = "CREATE_ITEM_ACTIVITY";

    private EditText edit_description;
    private EditText edit_due_date;
    private EditText edit_amount;
    private DatePickerDialog date_picker;
    private String date_selected;

    private int day = 0;
    private int month = 0;
    private int year = 0;

    private SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        edit_description = findViewById(R.id.edit_description);
        edit_due_date = findViewById(R.id.edit_due_date);
        edit_amount = findViewById(R.id.edit_amount);

        edit_due_date.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                date_selected = FORMAT.format(new GregorianCalendar(year, month, day).getTime());
                // date picker dialog
                date_picker = new DatePickerDialog(CreateDigsitItemActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edit_due_date.setText(FORMAT.format(new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime()));
                                date_selected = FORMAT.format(new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime());
                            }
                        }, year, month, day);
                date_picker.show();
            }
        });

        //This will set amount visible if it is a create Fund activity
        final Intent intent_in = getIntent();
        if (intent_in.getStringExtra(CREATE_ITEM_ACTIVITY).equalsIgnoreCase("CREATE_FUND_ACTIVITY")) {
            edit_amount.setVisibility(View.VISIBLE);
        }

        //Gets params to determine which items it needs to create
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(edit_description.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String task_description;
                    double task_amount;
                    switch (intent_in.getStringExtra(CREATE_ITEM_ACTIVITY)) {
                        case "CREATE_TASK_ACTIVITY":
                            task_description = edit_description.getText().toString();

                            replyIntent.putExtra(CREATE_DESCRIPTION, task_description);
                            replyIntent.putExtra(CREATE_DUE_DATE, date_selected);
                            replyIntent.putExtra(CREATE_ITEM_ACTIVITY, TasksActivity.ACTIVITY_NAME);
                            setResult(RESULT_OK, replyIntent);
                            break;
                        case "CREATE_FUND_ACTIVITY":
                            task_description = edit_description.getText().toString();
                            task_amount = Double.parseDouble(edit_amount.getText().toString());

                            replyIntent.putExtra(CREATE_DESCRIPTION, task_description);
                            replyIntent.putExtra(CREATE_AMOUNT, task_amount);
                            replyIntent.putExtra(CREATE_DUE_DATE, date_selected);
                            replyIntent.putExtra(CREATE_ITEM_ACTIVITY, FundsActivity.ACTIVITY_NAME);
                            setResult(RESULT_OK, replyIntent);
                            break;
                        case "CREATE_GROCERY_ACTIVITY":
                            task_description = edit_description.getText().toString();

                            replyIntent.putExtra(CREATE_DESCRIPTION, task_description);
                            replyIntent.putExtra(CREATE_DUE_DATE, date_selected);
                            replyIntent.putExtra(CREATE_ITEM_ACTIVITY, GroceriesActivity.ACTIVITY_NAME);
                            setResult(RESULT_OK, replyIntent);
                            break;
                        case "CREATE_CALENDAR_ACTIVITY":
                            break;
                    }
                }
                finish();
            }
        });
    }

    public boolean filledInDate() {
        return !(this.day == 0 || this.month == 0 || this.year == 0);
    }



}

