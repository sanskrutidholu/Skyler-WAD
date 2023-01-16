package com.webandappdevelopment.skyler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.utility.DatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CartServiceDetailsActivity extends AppCompatActivity {

    ImageView serviceback, dateback, addressback ;
    TextView heading, tv_calender;
    Button next;
    private int mYear, mMonth, mDay, mHour, mMinute;
    RelativeLayout rl_service, rl_date, rl_address;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_service_details);

        serviceback = findViewById(R.id.serviceback);
        dateback = findViewById(R.id.dateback);
        addressback = findViewById(R.id.addressback);
        next = findViewById(R.id.btn_next);
        tv_calender = findViewById(R.id.tv_calender);
        rl_service = findViewById(R.id.rl_serviceDetails);
        rl_date = findViewById(R.id.rl_dateTime);
        rl_address = findViewById(R.id.rl_address);

        serviceback.setOnClickListener(view -> {
           startActivity(new Intent(this, MyCartActivity.class));
           finish();
        });

        dateback.setOnClickListener(view -> {
            startActivity(new Intent(this, MyCartActivity.class));
            finish();
        });

        addressback.setOnClickListener(view -> {
            startActivity(new Intent(this, MyCartActivity.class));
            finish();
        });

        tv_calender.setOnClickListener(view ->{
            getDate();
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onServiceScreen();
            }
        });
    }

    private void onServiceScreen() {
        rl_service.setVisibility(View.GONE);
        rl_date.setVisibility(View.VISIBLE);

        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onDateScreen();
                    }
                }
        );
    }

    private void onDateScreen() {
        rl_date.setVisibility(View.GONE);
        rl_service.setVisibility(View.GONE);
        rl_address.setVisibility(View.VISIBLE);

        next.setText("DONE");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddressScreen();
            }
        });
    }

    private void onAddressScreen() {
        Intent i = new Intent(CartServiceDetailsActivity.this, MyCartActivity.class);
        startActivity(i);
        finish();
    }

    private void getDate(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        tv_calender.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }

                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


}