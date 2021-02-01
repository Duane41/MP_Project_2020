package com.example.mp_project.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp_project.R;
import com.example.mp_project.database.DigsitViewModel;
import com.example.mp_project.database.objects.DigsitItem;
import com.example.mp_project.database.objects.Funds;
import com.example.mp_project.list_adapters.DigsitFundsListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;

public class FundsActivity extends AppCompatActivity implements DigsitFundsListAdapter.OnItemListener {
    private static final int NEW_DIGSIT_ITEM = 1;
    public static final String CREATE_ITEM_ACTIVITY= "CREATE_ITEM_ACTIVITY";
    public static final String ACTIVITY_NAME = "CREATE_FUND_ACTIVITY";

    private TextView I_Owe_field;
    private TextView DashTitle;
    private DigsitViewModel mDigsitItemViewModel;
    private DigsitFundsListAdapter mAdapter;
    private TextView title_users_list;
    private RecyclerView mRecyclerView;
    private DecimalFormat df = new DecimalFormat("#.##");
    private LifecycleOwner lfc;
    AlertDialog.Builder myAlertBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds);
        myAlertBuilder = new AlertDialog.Builder(FundsActivity.this);
        mDigsitItemViewModel = ViewModelProviders.of(this).get(DigsitViewModel.class);
        title_users_list = findViewById(R.id.funds_summary_list_title);
        title_users_list.setText("Summary: ");
        lfc = this;
        mRecyclerView = findViewById(R.id.recyclerview_fund_summary_item);
        mAdapter = new DigsitFundsListAdapter(this, mDigsitItemViewModel, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDigsitItemViewModel.getFunds(1);
        mDigsitItemViewModel.getFundsList().observe(this, new Observer<List<Funds>>() {
            @Override
            public void onChanged(@Nullable final List<Funds> funds) {
                mAdapter.setDigsitIems(funds);
            }
        });

        I_Owe_field = findViewById(R.id.funds_dash_total);
        DashTitle = findViewById(R.id.funds_dash_title);
        mDigsitItemViewModel.getIOwe(1);
        mDigsitItemViewModel.getIOwe().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable final Double amount) {
                if (amount.doubleValue() < 0) {
                    DashTitle.setText("You owe:");
                    I_Owe_field.setText("R" + df.format(amount.doubleValue()*-1));
                } else if (amount.doubleValue() > 0) {
                    DashTitle.setText("You're owed:");
                    I_Owe_field.setText("R" + df.format(amount.doubleValue()));
                } else {
                    DashTitle.setText("Settled Up!");
                }

            }
        });



        FloatingActionButton fab = findViewById(R.id.add_grocery_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FundsActivity.this, CreateDigsitItemActivity.class);
                intent.putExtra(CREATE_ITEM_ACTIVITY, ACTIVITY_NAME);
                startActivityForResult(intent, NEW_DIGSIT_ITEM);
            }
        });
    }

    @Override
    public void OnItemClick(int position) {
        final Funds fund_clicked = mAdapter.getDigsitItemAtPosition(position);

        if (fund_clicked.getAmount() == 0.0) return;
        myAlertBuilder.setNegativeButton("Cancel", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //myAlertBuilder.show();

        myAlertBuilder.setPositiveButton("OK", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        fund_clicked.setAmount(0.00);
                        mDigsitItemViewModel.updateFund(fund_clicked);
                        mDigsitItemViewModel.getIOwe(1);
                        mDigsitItemViewModel.getFunds(1);

                        Toast.makeText(getApplicationContext(), "Settled up!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        myAlertBuilder.setTitle("Settle up with " + fund_clicked.getName() + "?");
        myAlertBuilder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String from_activity = "";
        if (data != null) {
            from_activity = data.getStringExtra(CreateDigsitItemActivity.CREATE_ITEM_ACTIVITY);
        }
        //Creates a fund item
        if (resultCode == RESULT_OK && from_activity.equalsIgnoreCase(ACTIVITY_NAME)) {
            mDigsitItemViewModel.updateFundsItem(1,  data.getDoubleExtra(CreateDigsitItemActivity.CREATE_AMOUNT, 0.00));
            mDigsitItemViewModel.getIOwe(1);
            mDigsitItemViewModel.getFunds(1);
            DigsitItem digsit_item = new DigsitItem(data.getStringExtra(CreateDigsitItemActivity.CREATE_DESCRIPTION), data.getStringExtra(CreateDigsitItemActivity.CREATE_DUE_DATE), false, true, true);
            mDigsitItemViewModel.insertDigsitItem(digsit_item);
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    "Fund creation cancelled.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
