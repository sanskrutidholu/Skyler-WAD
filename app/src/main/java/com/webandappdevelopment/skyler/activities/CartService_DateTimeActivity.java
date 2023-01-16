package com.webandappdevelopment.skyler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.utility.CallToast;
import com.webandappdevelopment.skyler.utility.Const;
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector;
import com.webandappdevelopment.skyler.utility.ShowCustomDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CartService_DateTimeActivity extends AppCompatActivity{

    ImageView back;
    Button next;
    TextView tv_calender,tv_time;
    LinearLayout ll_time;
    AppCompatButton btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_10;

    String order_Date, time, todaysDate, userDate;
    int mYear, mMonth, mDay;

    InternetConnectionDetector icd;
    SharedPreferences orderPreference;
    SharedPreferences.Editor orderEditor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_service_date_time);

        findViewById(R.id.hide).setOnClickListener(this::closeKeyboard);

        setUpUi();
        onClicks();

        icd = new InternetConnectionDetector(this);
        orderPreference = getSharedPreferences("ORDER_DETAILS", MODE_PRIVATE);

        back.setOnClickListener(view -> {
            startActivity(new Intent(this, CartService_DetailsActivity.class));
            finish();
        });

        next.setOnClickListener(view ->  {

            time = tv_time.getText().toString();

            if (userDate != null && !time.equals("")){
                if (icd.isConnected()){
                    orderPreference = getSharedPreferences("ORDER_DETAILS", MODE_PRIVATE);
                    orderEditor = orderPreference.edit();
                    orderEditor.putString("DATE",userDate);
                    orderEditor.putString("TIME",time);
                    orderEditor.apply();

                    startActivity(new Intent(this, CartService_AddressActivity.class));
                    finish();
                }else{
                    new ShowCustomDialog(this);
                }

            }else{
                new CallToast(this,"Please Choose Date and Time..");            }
        });

        tv_calender.setOnClickListener(view ->{
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(android.widget.DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            order_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                            Date date = new Date();
                            Date enterDate = null;
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                enterDate = formatter.parse(order_Date);
                                SimpleDateFormat finalFormat = new SimpleDateFormat("dd-MM-yyyy");
                                todaysDate = finalFormat.format(date);
                                userDate = finalFormat.format(enterDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            tv_calender.setText(userDate);

                            if (userDate.equals(todaysDate)){
                                try {
                                    checkTime(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                btn_1.setEnabled(true);
                                btn_2.setEnabled(true);
                                btn_3.setEnabled(true);
                                btn_4.setEnabled(true);
                                btn_5.setEnabled(true);
                                btn_6.setEnabled(true);
                                btn_7.setEnabled(true);
                                btn_8.setEnabled(true);
                                btn_9.setEnabled(true);
                                btn_10.setEnabled(true);
                            }
                        }

                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

    }

    private void setUpUi(){
        back = findViewById(R.id.back);
        next = findViewById(R.id.btn_next);
        tv_calender = findViewById(R.id.tv_calender);
        ll_time = findViewById(R.id.ll_time);
        tv_time = findViewById(R.id.tv_time);
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_10 = findViewById(R.id.btn_10);
    }

    @SuppressLint("SetTextI18n")
    private void onClicks(){
        btn_1.setOnClickListener(view ->{
            btn_1.setBackgroundResource(R.drawable.onserach_bg);
            btn_2.setBackgroundResource(R.drawable.search_bg_white);
            btn_3.setBackgroundResource(R.drawable.search_bg_white);
            btn_4.setBackgroundResource(R.drawable.search_bg_white);
            btn_5.setBackgroundResource(R.drawable.search_bg_white);
            btn_6.setBackgroundResource(R.drawable.search_bg_white);
            btn_7.setBackgroundResource(R.drawable.search_bg_white);
            btn_8.setBackgroundResource(R.drawable.search_bg_white);
            btn_9.setBackgroundResource(R.drawable.search_bg_white);
            btn_10.setBackgroundResource(R.drawable.search_bg_white);
            tv_time.setText("09:00:00");
        });

        btn_2.setOnClickListener(view ->{
            btn_1.setBackgroundResource(R.drawable.search_bg_white);
            btn_2.setBackgroundResource(R.drawable.onserach_bg);
            btn_3.setBackgroundResource(R.drawable.search_bg_white);
            btn_4.setBackgroundResource(R.drawable.search_bg_white);
            btn_5.setBackgroundResource(R.drawable.search_bg_white);
            btn_6.setBackgroundResource(R.drawable.search_bg_white);
            btn_7.setBackgroundResource(R.drawable.search_bg_white);
            btn_8.setBackgroundResource(R.drawable.search_bg_white);
            btn_9.setBackgroundResource(R.drawable.search_bg_white);
            btn_10.setBackgroundResource(R.drawable.search_bg_white);
            tv_time.setText("10:00:00");
        });

        btn_3.setOnClickListener(view ->{
            btn_1.setBackgroundResource(R.drawable.search_bg_white);
            btn_2.setBackgroundResource(R.drawable.search_bg_white);
            btn_3.setBackgroundResource(R.drawable.onserach_bg);
            btn_4.setBackgroundResource(R.drawable.search_bg_white);
            btn_5.setBackgroundResource(R.drawable.search_bg_white);
            btn_6.setBackgroundResource(R.drawable.search_bg_white);
            btn_7.setBackgroundResource(R.drawable.search_bg_white);
            btn_8.setBackgroundResource(R.drawable.search_bg_white);
            btn_9.setBackgroundResource(R.drawable.search_bg_white);
            btn_10.setBackgroundResource(R.drawable.search_bg_white);
            tv_time.setText("11:00:00");
        });

        btn_4.setOnClickListener(view ->{
            btn_1.setBackgroundResource(R.drawable.search_bg_white);
            btn_2.setBackgroundResource(R.drawable.search_bg_white);
            btn_3.setBackgroundResource(R.drawable.search_bg_white);
            btn_4.setBackgroundResource(R.drawable.onserach_bg);
            btn_5.setBackgroundResource(R.drawable.search_bg_white);
            btn_6.setBackgroundResource(R.drawable.search_bg_white);
            btn_7.setBackgroundResource(R.drawable.search_bg_white);
            btn_8.setBackgroundResource(R.drawable.search_bg_white);
            btn_9.setBackgroundResource(R.drawable.search_bg_white);
            btn_10.setBackgroundResource(R.drawable.search_bg_white);
            tv_time.setText("12:00:00");
        });

        btn_5.setOnClickListener(view ->{
            btn_1.setBackgroundResource(R.drawable.search_bg_white);
            btn_2.setBackgroundResource(R.drawable.search_bg_white);
            btn_3.setBackgroundResource(R.drawable.search_bg_white);
            btn_4.setBackgroundResource(R.drawable.search_bg_white);
            btn_5.setBackgroundResource(R.drawable.onserach_bg);
            btn_6.setBackgroundResource(R.drawable.search_bg_white);
            btn_7.setBackgroundResource(R.drawable.search_bg_white);
            btn_8.setBackgroundResource(R.drawable.search_bg_white);
            btn_9.setBackgroundResource(R.drawable.search_bg_white);
            btn_10.setBackgroundResource(R.drawable.search_bg_white);
            tv_time.setText("13:00:00");
        });

        btn_6.setOnClickListener(view ->{
            btn_1.setBackgroundResource(R.drawable.search_bg_white);
            btn_2.setBackgroundResource(R.drawable.search_bg_white);
            btn_3.setBackgroundResource(R.drawable.search_bg_white);
            btn_4.setBackgroundResource(R.drawable.search_bg_white);
            btn_5.setBackgroundResource(R.drawable.search_bg_white);
            btn_6.setBackgroundResource(R.drawable.onserach_bg);
            btn_7.setBackgroundResource(R.drawable.search_bg_white);
            btn_8.setBackgroundResource(R.drawable.search_bg_white);
            btn_9.setBackgroundResource(R.drawable.search_bg_white);
            btn_10.setBackgroundResource(R.drawable.search_bg_white);
            tv_time.setText("14:00:00");
        });

        btn_7.setOnClickListener(view ->{
            btn_1.setBackgroundResource(R.drawable.search_bg_white);
            btn_2.setBackgroundResource(R.drawable.search_bg_white);
            btn_3.setBackgroundResource(R.drawable.search_bg_white);
            btn_4.setBackgroundResource(R.drawable.search_bg_white);
            btn_5.setBackgroundResource(R.drawable.search_bg_white);
            btn_6.setBackgroundResource(R.drawable.search_bg_white);
            btn_7.setBackgroundResource(R.drawable.onserach_bg);
            btn_8.setBackgroundResource(R.drawable.search_bg_white);
            btn_9.setBackgroundResource(R.drawable.search_bg_white);
            btn_10.setBackgroundResource(R.drawable.search_bg_white);
            tv_time.setText("15:00:00");
        });

        btn_8.setOnClickListener(view ->{
            btn_1.setBackgroundResource(R.drawable.search_bg_white);
            btn_2.setBackgroundResource(R.drawable.search_bg_white);
            btn_3.setBackgroundResource(R.drawable.search_bg_white);
            btn_4.setBackgroundResource(R.drawable.search_bg_white);
            btn_5.setBackgroundResource(R.drawable.search_bg_white);
            btn_6.setBackgroundResource(R.drawable.search_bg_white);
            btn_7.setBackgroundResource(R.drawable.search_bg_white);
            btn_8.setBackgroundResource(R.drawable.onserach_bg);
            btn_9.setBackgroundResource(R.drawable.search_bg_white);
            btn_10.setBackgroundResource(R.drawable.search_bg_white);
            tv_time.setText("16:00:00");
        });

        btn_9.setOnClickListener(view ->{
            btn_1.setBackgroundResource(R.drawable.search_bg_white);
            btn_2.setBackgroundResource(R.drawable.search_bg_white);
            btn_3.setBackgroundResource(R.drawable.search_bg_white);
            btn_4.setBackgroundResource(R.drawable.search_bg_white);
            btn_5.setBackgroundResource(R.drawable.search_bg_white);
            btn_6.setBackgroundResource(R.drawable.search_bg_white);
            btn_7.setBackgroundResource(R.drawable.search_bg_white);
            btn_8.setBackgroundResource(R.drawable.search_bg_white);
            btn_9.setBackgroundResource(R.drawable.onserach_bg);
            btn_10.setBackgroundResource(R.drawable.search_bg_white);
            tv_time.setText("17:00:00");
        });

        btn_10.setOnClickListener(view ->{
            btn_1.setBackgroundResource(R.drawable.search_bg_white);
            btn_2.setBackgroundResource(R.drawable.search_bg_white);
            btn_3.setBackgroundResource(R.drawable.search_bg_white);
            btn_4.setBackgroundResource(R.drawable.search_bg_white);
            btn_5.setBackgroundResource(R.drawable.search_bg_white);
            btn_6.setBackgroundResource(R.drawable.search_bg_white);
            btn_7.setBackgroundResource(R.drawable.search_bg_white);
            btn_8.setBackgroundResource(R.drawable.search_bg_white);
            btn_9.setBackgroundResource(R.drawable.search_bg_white);
            btn_10.setBackgroundResource(R.drawable.onserach_bg);
            tv_time.setText("18:00:00");
        });

    }

    private void checkTime(Date enterDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");
        String time = dateFormat.format(enterDate);
        Log.e("","time"+time);

        if (dateFormat.parse(time).after(dateFormat.parse("18:00:00"))) {
            btn_1.setEnabled(false);
            btn_2.setEnabled(false);
            btn_3.setEnabled(false);
            btn_4.setEnabled(false);
            btn_5.setEnabled(false);
            btn_6.setEnabled(false);
            btn_7.setEnabled(false);
            btn_8.setEnabled(false);
            btn_9.setEnabled(false);
            btn_10.setEnabled(false);
        }else if (dateFormat.parse(time).after(dateFormat.parse("17:00:00"))){
                btn_1.setEnabled(false);
                btn_2.setEnabled(false);
                btn_3.setEnabled(false);
                btn_4.setEnabled(false);
                btn_5.setEnabled(false);
                btn_6.setEnabled(false);
                btn_7.setEnabled(false);
                btn_8.setEnabled(false);
                btn_9.setEnabled(false);
                btn_10.setEnabled(true);
        }else if (dateFormat.parse(time).after(dateFormat.parse("16:00:00"))){
            btn_1.setEnabled(false);
            btn_2.setEnabled(false);
            btn_3.setEnabled(false);
            btn_4.setEnabled(false);
            btn_5.setEnabled(false);
            btn_6.setEnabled(false);
            btn_7.setEnabled(false);
            btn_8.setEnabled(false);
            btn_9.setEnabled(true);
            btn_10.setEnabled(true);
        }else if (dateFormat.parse(time).after(dateFormat.parse("15:00:00"))){
            btn_1.setEnabled(false);
            btn_2.setEnabled(false);
            btn_3.setEnabled(false);
            btn_4.setEnabled(false);
            btn_5.setEnabled(false);
            btn_6.setEnabled(false);
            btn_7.setEnabled(false);
            btn_8.setEnabled(true);
            btn_9.setEnabled(true);
            btn_10.setEnabled(true);
        }else if (dateFormat.parse(time).after(dateFormat.parse("14:00:00"))){
            btn_1.setEnabled(false);
            btn_2.setEnabled(false);
            btn_3.setEnabled(false);
            btn_4.setEnabled(false);
            btn_5.setEnabled(false);
            btn_6.setEnabled(false);
            btn_7.setEnabled(true);
            btn_8.setEnabled(true);
            btn_9.setEnabled(true);
            btn_10.setEnabled(true);
        }else if (dateFormat.parse(time).after(dateFormat.parse("13:00:00"))){
            btn_1.setEnabled(false);
            btn_2.setEnabled(false);
            btn_3.setEnabled(false);
            btn_4.setEnabled(false);
            btn_5.setEnabled(false);
            btn_6.setEnabled(true);
            btn_7.setEnabled(true);
            btn_8.setEnabled(true);
            btn_9.setEnabled(true);
            btn_10.setEnabled(true);
        }else if (dateFormat.parse(time).after(dateFormat.parse("12:00:00"))){
            btn_1.setEnabled(false);
            btn_2.setEnabled(false);
            btn_3.setEnabled(false);
            btn_4.setEnabled(false);
            btn_5.setEnabled(true);
            btn_6.setEnabled(true);
            btn_7.setEnabled(true);
            btn_8.setEnabled(true);
            btn_9.setEnabled(true);
            btn_10.setEnabled(true);
        }else if (dateFormat.parse(time).after(dateFormat.parse("11:00:00"))){
            btn_1.setEnabled(false);
            btn_2.setEnabled(false);
            btn_3.setEnabled(false);
            btn_4.setEnabled(true);
            btn_5.setEnabled(true);
            btn_6.setEnabled(true);
            btn_7.setEnabled(true);
            btn_8.setEnabled(true);
            btn_9.setEnabled(true);
            btn_10.setEnabled(true);
        }else if (dateFormat.parse(time).after(dateFormat.parse("10:00:00"))){
            btn_1.setEnabled(false);
            btn_2.setEnabled(false);
            btn_3.setEnabled(true);
            btn_4.setEnabled(true);
            btn_5.setEnabled(true);
            btn_6.setEnabled(true);
            btn_7.setEnabled(true);
            btn_8.setEnabled(true);
            btn_9.setEnabled(true);
            btn_10.setEnabled(true);
        }  else if (dateFormat.parse(time).after(dateFormat.parse("09:00:00"))){
            btn_1.setEnabled(false);
            btn_2.setEnabled(true);
            btn_3.setEnabled(true);
            btn_4.setEnabled(true);
            btn_5.setEnabled(true);
            btn_6.setEnabled(true);
            btn_7.setEnabled(true);
            btn_8.setEnabled(true);
            btn_9.setEnabled(true);
            btn_10.setEnabled(true);
        } else {
            new CallToast(this,"Sorry , Please on other date..");        }

    }

    private void closeKeyboard(View view) {
        view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}